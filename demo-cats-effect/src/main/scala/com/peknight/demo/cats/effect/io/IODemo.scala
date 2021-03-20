package com.peknight.demo.cats.effect.io

import cats.data.NonEmptyList
import cats.effect.ExitCase.{Canceled, Completed, Error}
import cats.effect.{ContextShift, ExitCase, IO, SyncIO, Timer}
import cats.syntax.flatMap._
import cats.syntax.parallel._

import java.io._
import java.util.concurrent.{Executors, ScheduledExecutorService}
import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{CancellationException, ExecutionContext, Future}
import scala.io.Source
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object IODemo extends App {

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

   // IO.cancelBoundary

  def fibWithCancelBoundary(n: Int, a: Long, b: Long): IO[Long] =
    IO.suspend {
      if (n <= 0) IO.pure(a) else {
        val next = fibWithCancelBoundary(n - 1, b, a + b)
        if (n % 100 == 0) IO.cancelBoundary *> next
        else next
      }
    }

  // Race Conditions -- race & racePair

//  def race[A, B](lh: IO[A], rh: IO[B])(implicit cs: ContextShift[IO]): IO[Either[A, B]]
//  def racePair[A, B](lh: IO[A], rh: IO[B])(implicit cs: ContextShift[IO]): IO[Either[(A, Fiber[IO, B]), (Fiber[IO, A], B)]

  def race[A, B](lh: IO[A], rh: IO[B])(implicit cs: ContextShift[IO]): IO[Either[A, B]] = {
    IO.racePair(lh, rh).flatMap {
      case Left((a, fiber)) => fiber.cancel.map(_ => Left(a))
      case Right((fiber, b)) => fiber.cancel.map(_ => Right(b))
    }
  }

  def timeoutTo[A](fa: IO[A], after: FiniteDuration, fallback: IO[A])
                  (implicit timer: Timer[IO], cs: ContextShift[IO]): IO[A] = {
    IO.race(fa, timer.sleep(after)).flatMap {
      case Left(a) => IO.pure(a)
      case Right(_) => fallback
    }
  }

  def timeout[A](fa: IO[A], after: FiniteDuration)(implicit timer: Timer[IO], cs: ContextShift[IO]): IO[A] = {
    val error = new CancellationException(after.toString)
    timeoutTo(fa, after, IO.raiseError(error))
  }

  def javaReadFirstLine(file: File): String = {
    val in = new BufferedReader(new FileReader(file))
    try {
      in.readLine()
    } finally {
      in.close()
    }
  }

  // bracket

  def readFirstLine(file: File): IO[String] =
    IO(new BufferedReader(new FileReader(file))).bracket { in =>
      IO(in.readLine())
    } { in =>
      IO(in.close()).void
    }

  {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    def readFile(file: File): IO[String] = {
      val acquire = IO.shift *> IO(new BufferedReader(new FileReader(file)))
      acquire.bracket { in =>
        IO {
          val content = new StringBuilder()
          var line: String = null
          do {
            line = in.readLine()
            if (line != null) content.append(line)
          } while (line != null)
          content.toString()
        }
      } { in =>
        IO(in.close()).void
      }
    }

    def readFileV2(file: File): IO[String] = {
      val acquire = IO.shift *> IO(new BufferedReader(new FileReader(file)))
      IO.suspend {
        var isCanceled = false

        acquire.bracket { in =>
          IO {
            val content = new StringBuilder()
            var line: String = null
            do {
              line = in.synchronized {
                if (!isCanceled) in.readLine()
                else null
              }
              if (line != null) {
                content.append(line)
              }
            } while (line != null)
            content.toString()
          }
        } { in =>
          IO {
            in.synchronized {
              isCanceled = true
              in.close()
            }
          }.void
        }
      }
    }

    def readLineV3(in: BufferedReader): IO[String] =
      IO.pure(in).bracketCase { in =>
        IO(in.readLine())
      } {
        case (_, Completed | Error(_)) =>
          IO.unit
        case (in, Canceled) =>
          IO(in.close())
      }
  }

  // Conversions

//  def fromFuture[A](iof: IO[Future[A]]): IO[A] = ???

  {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    println(IO.fromFuture(IO {
      Future(println("I come from the Future!"))
    }).unsafeRunSync())

    val f = Future.successful("I come from the Future!")
    println(IO.fromFuture(IO.pure(f)).unsafeRunSync())
  }

//  def fromEither[A](e: Either[Throwable, A]): IO[A] = e.fold(IO.raiseError, IO.pure)

  // Error Handling

  val boom: IO[Unit] = IO.raiseError(new Exception("boom"))
//  boom.unsafeRunSync()

  boom.attempt.unsafeRunSync()

  // Example: Retrying with Exponential Backoff 二进制指数退避

  def retryWithBackoff[A](ioa: IO[A], initialDelay: FiniteDuration, maxRetries: Int)
                         (implicit timer: Timer[IO]): IO[A] = {
    ioa.handleErrorWith { error =>
      if (maxRetries > 0)
        IO.sleep(initialDelay) *> retryWithBackoff(ioa, initialDelay * 2, maxRetries - 1)
      else
        IO.raiseError(error)
    }
  }

  // Thread Shifting

  {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val contextShift: ContextShift[IO] = IO.contextShift(global)

    val task = IO(println("task"))
    IO.shift(contextShift).flatMap(_ => task)

    // Cats syntax
    IO.shift *> task
    implicitly[ContextShift[IO]].shift *> task

    task.flatMap(a => IO.shift.map(_ => a))

    // Cats syntax
    task <* IO.shift
    task <* implicitly[ContextShift[IO]].shift

    val cachedThreadPool = Executors.newCachedThreadPool()
    val BlockingFileIO = ExecutionContext.fromExecutor(cachedThreadPool)
    implicit val Main = ExecutionContext.global

    val ioa: IO[Unit] = for {
      _ <- IO(println("Enter your name: "))
      _ <- IO.shift(BlockingFileIO)
//      name <- IO(scala.io.StdIn.readLine())
      _ <- IO.shift(Main)
//      _ <- IO(println(s"Welcome $name!"))
      _ <- IO(cachedThreadPool.shutdown())
    } yield ()
    ioa.unsafeRunSync()

    lazy val doStuff = IO(println("stuff"))

    lazy val repeat: IO[Unit] = for {
      _ <- doStuff
      _ <- IO.shift
      _ <- repeat
    } yield ()

  }

  def signal[A](a: A): IO[A] = IO.async(_(Right(a)))

  def loop(n: Int): IO[Int] = signal(n).flatMap { x =>
    if (x > 0) loop(n - 1) else IO.pure(0)
  }

  {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    val ioA = IO(println("Running ioA"))
    val ioB = IO(println("Running ioB"))
    val ioC = IO(println("Running ioC"))

    val program = (ioA, ioB, ioC).parMapN { (_, _, _) => ()}
    program.unsafeRunSync()

    val a = IO.raiseError[Unit](new Exception("boom")) <* IO(println("Running ioA"))
    val b = (IO.sleep(1.second) *> IO(println("Running ioB")))
      .guaranteeCase {
        case ExitCase.Canceled => IO(println("ioB was canceled!"))
        case _ => IO.unit
      }

    val parFailure = (a, b).parMapN { (_, _) => () }

    parFailure.attempt.unsafeRunSync()

    val ioA2 = IO.sleep(10.seconds) *> IO(println("Delayed!"))
    val ioB2 = IO.raiseError[Unit](new Exception("dummy"))

//    (ioA2, ioB2).parMapN((_, _) => ()).unsafeRunSync()

    val anIO = IO(1)

    val aLotOfIOs = NonEmptyList.of(anIO, anIO)

    val ioOfList = aLotOfIOs.parSequence

    val results = NonEmptyList.of(1, 2, 3).parTraverse { i =>
      IO(i)
    }
  }

  // "Unsafe" Operations

  // unsafeRunSync

  IO(println("Sync!")).unsafeRunSync()

  // unsafeRunAsync

  IO(println("Async!")).unsafeRunAsync(_ => ())

  // unsafeRunCancelable

  IO(println("Potentially cancelable!")).unsafeRunCancelable(_ => ())

  // unsafeRunTimed * for testing purposes, never appear in mainline production code
  IO(println("Timed!")).unsafeRunTimed(5.seconds)

  // unsafeToFuture
  println(IO("Gimme a Future!").unsafeToFuture())
}
