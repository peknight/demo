package com.peknight.demo.cats.effect.io

import cats.effect.{ContextShift, Fiber, IO, SyncIO}
import cats.syntax.flatMap._

import java.io.{BufferedReader, File, FileInputStream, InputStreamReader}
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object IOApp extends App {

  // Introduction

  val ioa = IO { println("hey!") }

  val program: IO[Unit] = for {
    _ <- ioa
    _ <- ioa
  } yield ()
  program.unsafeRunSync()

  // On Referential Transparency and Lazy Evaluation

  def addToGauge(value: Int): IO[Unit] = IO { println(value) }

  (for {
    _ <- addToGauge(32)
    _ <- addToGauge(32)
  } yield ()).unsafeRunSync()

  val task = addToGauge(32)

  (for {
    _ <- task
    _ <- task
  } yield ()).unsafeRunSync()

  // Stack Safety

  def fib(n: Int, a: Long = 0, b: Long = 1): IO[Long] =
    IO(a + b).flatMap { b2 =>
      if (n > 0) fib(n - 1, b, b2)
      else IO.pure(a)
    }

  println(fib(5).unsafeRunSync())

  // Describing Effects

  // Pure Values -- IO.pure & IO.unit

  // passed "by value"
  IO.pure(25).flatMap(n => IO(println(s"Number is :$n"))).unsafeRunSync()

  IO.pure(println("THIS IS WRONG!"))

  // IO.unit is simply an alias for IO.pure(())
  val unit: IO[Unit] = IO.pure(())

  // Synchronous Effects -- IO.apply

  // IO.apply passed "by name"
  def putStrLn(value: String) = IO(println(value))
  val readLn = IO(scala.io.StdIn.readLine())

  (for {
    _ <- putStrLn("What's your name? (need input)")
//    n <- readLn
//    _ <- putStrLn(s"Hello, $n!")
  } yield ()).unsafeRunSync()

  // Asynchronous Effects -- IO.async & IO.cancelable

  def convert[A](fa: => Future[A])(implicit ec: ExecutionContext): IO[A] = {
    IO.async { callback: (Either[Throwable, A] => Unit) =>
      fa.onComplete {
        case Success(a) => callback(Right(a))
        case Failure(e) => callback(Left(e))
      }
    }
  }

  // Use IO.sleep
  def delayedTick(d: FiniteDuration)(implicit sc: ScheduledExecutorService): IO[Unit] = {
    IO.cancelable { callback: (Either[Throwable, Unit] => Unit) =>
      val r = new Runnable {
        def run(): Unit = callback(Right(()))
      }
      val f = sc.schedule(r, d.length, d.unit)
      IO(f.cancel(false)).void
    }
  }

  // IO.never
  val never: IO[Nothing] = IO.async(_ => ())

//  IO.race(lh, IO.never) <-> lh.map(Left(_))
//  IO.race(IO.never, rh) <-> rh.map(Right(_))


  // Deferred Execution -- IO.suspend

  //  IO.suspend(f) <-> IO(f).flatten
  def fibWithSuspend(n: Int, a: Long, b: Long): IO[Long] =
    IO.suspend {
      if (n > 0) fibWithSuspend(n - 1, b, a + b)
      else IO.pure(a)
    }

  println(fibWithSuspend(5, 0, 1).unsafeRunSync())

  // TODO ContextShift是干什么用的
  def fibWithContextShift(n: Int, a: Long, b: Long)(implicit cs: ContextShift[IO]): IO[Long] =
    IO.suspend {
      if (n == 0) IO.pure(a) else {
        val next = fibWithContextShift(n - 1, b, a + b)
        if (n % 100 == 0) cs.shift *> next
        else next
      }
    }

  {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    def retryUntilRight[A, B](io: IO[Either[A, B]]): IO[B] = {
      io.flatMap {
        case Right(b) => IO.pure(b)
        case Left(_) => retryUntilRight(io)
      }
    }

    val notCancelable: IO[Int] = retryUntilRight(IO(Left(0)))

    val cancelable: IO[Int] = IO.shift *> retryUntilRight(IO(Left(0)))
  }

  // Building cancelable IO tasks

  def unsafeFileToString(file: File): String = {
    val in = new BufferedReader(
      new InputStreamReader(new FileInputStream(file), "utf-8")
    )
    try {
      val sb = new StringBuilder()
      var hasNext = true
      while (hasNext) {
        hasNext = false
        val line = in.readLine()
        if (line != null) {
          hasNext = true
          sb.append(line)
        }
      }
      sb.toString()
    } finally {
      in.close()
    }
  }

  def readFile(file: File)(implicit ec: ExecutionContext) =
    IO.async[String] { callback =>
      ec.execute(() => {
        try {
          callback(Right(unsafeFileToString(file)))
        } catch {
          case NonFatal(e) => callback(Left(e))
        }
      })
    }

  def unsafeFileToStringV2(file: File, isActive: AtomicBoolean) = {
    val sc = new StringBuilder
    val linesIterator = Source.fromFile(file).getLines()
    var hasNext = true
    while (hasNext && isActive.get) {
      sc.append(linesIterator.next())
      hasNext = linesIterator.hasNext
    }
    sc.toString()
  }

  def readFileV2(file: File)(implicit ec: ExecutionContext) =
    IO.cancelable[String] { callback =>
      val isActive = new AtomicBoolean(true)
      ec.execute(() => {
        try {
          callback(Right(unsafeFileToStringV2(file, isActive)))
        } catch {
          case NonFatal(e) => callback(Left(e))
        }
      })
      IO(isActive.set(false)).void
    }

  def readLine(in: BufferedReader)(implicit ec: ExecutionContext) =
    IO.cancelable[String] { callback =>
      ec.execute(() => callback(
        try Right(in.readLine())
        catch { case NonFatal(e) => Left(e) }
      ))
      IO(in.close()).void
    }

  def readLineV2(in: BufferedReader)(implicit ec: ExecutionContext) =
    IO.cancelable[String] { callback =>
      val isActive = new AtomicBoolean(true)
      ec.execute { () =>
        if (isActive.getAndSet(false)) {
          try callback(Right(in.readLine()))
          catch { case NonFatal(e) => callback(Left(e)) }
        }
      }
      IO {
        if (isActive.getAndSet(false)) {
          in.close()
        }
      }.void

    }


  {
    // Concurrent start + cancel

    // Needed for IO.start to do a logical thread fork
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    val launchMissiles: IO[Unit] = IO.raiseError(new Exception("boom!"))
    val runToBunker = IO(println("To the bunker!!!"))

    val aftermathRes = for {
      fiber <- launchMissiles.start
      _ <- runToBunker.handleErrorWith { error =>
        // Retreat failed, cancel launch (maybe we should
        // have retreated to our bunker before the launch?)
        fiber.cancel *> IO.raiseError(error)
      }
      aftermath <- fiber.join
    } yield aftermath

//    aftermathRes.unsafeRunSync()
  }


  // runCancelable & unsafeRunCancelable

  implicit val timer = IO.timer(ExecutionContext.global)
  val io: IO[Unit] = IO.sleep(3.seconds) *> IO(println("Hello!"))

  val cancel: IO[Unit] = io.unsafeRunCancelable(r => println(s"Done $r"))

  cancel.unsafeRunSync()
  // TODO 暂时没看懂这是在干啥


  val pureResult: SyncIO[IO[Unit]] = io.runCancelable { r =>
    IO(println(s"Done: $r"))
  }

  pureResult.toIO.flatten.unsafeRunSync()

  // uncancelable marker

  io.uncancelable.unsafeRunSync()

  


}
