package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.iomonad.App.{IO, unsafePerformIO}
import com.peknight.demo.fpinscala.iomonad.Free.{Return, Suspend}
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
import com.peknight.demo.fpinscala.streamingio.T.{L, R}

import java.io.FileWriter
import java.util.concurrent.Executors
import scala.annotation.tailrec

/**
 * GeneralizeStreamTransducers
 */
trait Process[F[_], O] {
  import Process.*

  def map[O2](f: O => O2): Process[F, O2] = this match {
    case Await(req, recv) => Await(req, recv andThen (_.map(f)))
    case Emit(h, t) => Try { Emit(f(h), t map f)}
    case Halt(err) => Halt(err)
  }

  def ++(p: => Process[F, O]): Process[F, O] = this.onHalt {
    case End => p
    case err => Halt(err)
  }

  // 和++操作一样，只是这里会始终执行p，哪怕是因为错误而停止的
  def onComplete(p: => Process[F, O]): Process[F, O] = this.onHalt {
    case End => p.asFinalizer
    // 这里的区别
    case err => p.asFinalizer ++ Halt(err)
  }

  def asFinalizer: Process[F, O] = this match {
    case Emit(h, t) => Emit(h, t.asFinalizer)
    case Halt(e) => Halt(e)
    case Await(req, recv) => await(req) {
      // 让一个普通的Process被Kill时调用自身
      case Left(Kill) => this.asFinalizer
      case x => recv(x)
    }
  }

  def onHalt(f: Throwable => Process[F, O]): Process[F, O] = this match {
    case Halt(e) => Try(f(e))
    case Emit(h, t) => Emit(h, t.onHalt(f))
    case Await(req, recv) => Await(req, recv.andThen(_.onHalt(f)))
  }

  def flatMap[O2](f: O => Process[F, O2]): Process[F, O2] = this match {
    case Halt(err) => Halt(err)
    case Emit(o, t) => Try(f(o)) ++ t.flatMap(f)
    case Await(req, recv) => Await(req, recv.andThen(_.flatMap(f)))
  }

  def repeat: Process[F, O] = this ++ this.repeat

  // Exercise 15.10
  def runLog(implicit F: MonadCatch[F]): F[IndexedSeq[O]] = {
    def go(cur: Process[F, O], acc: IndexedSeq[O]): F[IndexedSeq[O]] = cur match {
      case Emit(h, t) => go(t, acc :+ h)
      case Halt(End) => F.unit(acc)
      case Halt(err) => F.fail(err)
      // 这里的实现是没法进行尾递归优化的，完全依赖底层的monad来保证栈安全
      case Await(req, recv) => F.flatMap(F.attempt(req)) {e => go(Try(recv(e)), acc)}
    }
    go(this, IndexedSeq())
  }

  def |>[O2](p2: Process1[O, O2]): Process[F, O2] = p2 match {
    case Halt(e) => this.kill onHalt { e2 => Halt(e) ++ Halt(e2) }
    case Emit(h, t) => Emit(h, this |> t)
    case Await(req, recv) => this match {
      case Halt(err) => Halt(err) |> recv(Left(err))
      case Emit(h, t) => t |> Try(recv(Right(h)))
      case Await(req0, recv0) => await(req0)(recv0.andThen(_ |> p2))
    }
  }

  // 给最外层的Await传递Kill异常，但忽略余下的输出
  @tailrec
  final def kill[O2]: Process[F, O2] = this match {
    case Await(_, recv) => recv(Left(Kill)).drain.onHalt {
      case Kill => Halt(End)
      case e => Halt(e)
    }
    case Halt(e) => Halt(e)
    case Emit(_, t) => t.kill
  }

  def pipe[O2](p2: Process1[O, O2]): Process[F, O2] = this |> p2

  final def drain[O2]: Process[F, O2] = this match {
    case Halt(e) => Halt(e)
    case Emit(h, t) => t.drain
    case Await(req, recv) => Await(req, recv.andThen(_.drain))
  }

  def filter(f: O => Boolean): Process[F, O] = this |> Process.filter(f)

  def take(n: Int): Process[F, O] = this |> Process.take(n)

  def once: Process[F, O] = take(1)

  def tee[O2, O3](p2: Process[F, O2])(t: Tee[O, O2, O3]): Process[F, O3] = t match {
    case Halt(e) => this.kill.onComplete(p2.kill.onComplete(Halt(e)))
    case Emit(h, t) => Emit(h, this.tee(p2)(t))
    case Await(side, recv) => side.get match {
      case Left(_) => this match {
        case Halt(e) => p2.kill.onComplete(Halt(e))
        case Emit(o, ot) => ot.tee(p2)(Try(recv(Right(o))))
        case Await(reqL, recvL) => await(reqL)(recvL.andThen(this2 => this2.tee(p2)(t)))
      }
      case Right(_) => p2 match {
        case Halt(e) => this.kill.onComplete(Halt(e))
        case Emit(o2, ot) => this.tee(ot)(Try(recv(Right(o2))))
        case Await(reqR, recvR) => await(reqR)(recvR.andThen(p3 => this.tee(p3)(t)))
      }
    }
  }

  def zipWith[O2, O3](p2: Process[F, O2])(f: (O, O2) => O3): Process[F, O3] = this.tee(p2)(Process.zipWith(f))

  def zip[O2](p2: Process[F, O2]): Process[F, (O, O2)] = zipWith(p2)((_, _))

  def to[O2](sink: Sink[F, O]): Process[F, Unit] = join { this.zipWith(sink)((o, f) => f(o)) }

  def through[O2](p2: Process[F, O => Process[F, O2]]): Process[F, O2] = join { this.zipWith(p2)((o, f) => f(o)) }
}
object Process {
  // recv函数应该是以trampolined的方式返回TailRec[Process[F, O]]，从而避免栈溢出的问题，但为了保持简单，这里并未这么做
  case class Await[F[_], A, O](req: F[A], recv: Either[Throwable, A] => Process[F, O]) extends Process[F, O]
  case class Emit[F[_], O](head: O, tail: Process[F, O]) extends Process[F, O]
  // Halt due to err, which could be an actual error or End indicating normal termination.
  case class Halt[F[_], O](err: Throwable) extends Process[F, O]

  case object End extends Exception:
    given CanEqual[End, Throwable]

  case object Kill extends Exception:
    given CanEqual[End, Throwable]

  def emit[F[_], O](head: O, tail: Process[F, O] = Halt[F, O](End)): Process[F, O] = Emit(head, tail)
  def await[F[_], A, O](req: F[A])(recv: Either[Throwable, A] => Process[F, O]): Process[F, O] = Await(req, recv)

  /* Helper function to safely produce p, or gracefully halt with an error if an exception is thrown. */
  def Try[F[_], O](p: => Process[F, O]): Process[F, O] = try p catch { case e: Throwable => Halt(e) }

  def runLog[O](src: Process[IO, O]): IO[IndexedSeq[O]] = Suspend(Par.lazyUnit {
    val E = Executors.newFixedThreadPool(4)
    def go(cur: Process[IO, O], acc: IndexedSeq[O]): IndexedSeq[O] = cur match {
      case Emit(h, t) => go(t, acc :+ h)
      case Halt(End) => acc
      case Halt(err) => throw err
      case Await(req, recv) => {
        val next = try recv(Right(unsafePerformIO(req)(E))) catch { case err: Throwable => recv(Left(err)) }
        go(next, acc)
      }
    }
    try go(src, IndexedSeq()) finally E.shutdown()
  })

  def resource[R, O](acquire: IO[R])(use: R => Process[IO, O])(release: R => Process[IO, O]): Process[IO, O] =
    await[IO, R, O](acquire) {
      case Left(err) => Halt(err)
      case Right(r) => use(r).onComplete(release(r))
    }

  /*
   * Like `resource`, but `release` is a single `IO` action.
   */
  def resource_[R, O](acquire: IO[R])(use: R => Process[IO, O])(release: R => IO[Unit]): Process[IO, O] =
    resource(acquire)(use)(release.andThen(eval_[IO, Unit, O]))

  def eval[F[_], A](a: F[A]): Process[F, A] = await[F, A, A](a) {
    case Left(err) => Halt(err)
    case Right(r) => Emit(r, Halt(End))
  }

  def eval_[F[_], A, B](a: F[A]): Process[F, B] = eval(a).drain[B]

  def lines(filename: String): Process[IO, String] =
    resource { Suspend(Par.lazyUnit(io.Source.fromFile(filename))) } { src =>
      // a stateful iterator
      lazy val iter = src.getLines()
      def step = if (iter.hasNext) Some(iter.next()) else None
      lazy val lines: Process[IO, String] = eval[IO, Option[String]](Suspend(Par.lazyUnit(step))).flatMap {
        case None => Halt(End)
        case Some(line) => Emit(line, lines)
      }
      lines
    } { src => eval_(Suspend(Par.lazyUnit(src.close()))) }

  type Process1[I, O] = Process[Is[I]#f, O]

  def Get[I]: Is[I]#f[I] = Is[I]().Get

  def await1[I, O](recv: I => Process1[I, O], fallback: Process1[I, O] = halt1[I, O]): Process1[I, O] = Await(Get[I],
    (e: Either[Throwable, I]) => e match {
      case Left(End) => fallback
      case Left(err) => Halt(err)
      case Right(i) => Try(recv(i))
    }
  )

  def emit1[I, O](h: O, t1: Process1[I, O] = halt1[I, O]): Process1[I, O] = emit(h, t1)

  def halt1[I, O]: Process1[I, O] = Halt[Is[I]#f, O](End)

  def lift[I, O](f: I => O): Process1[I, O] = await1[I, O](i => emit(f(i))).repeat

  def filter[I](f: I => Boolean): Process1[I, I] = await1[I, I](i => if (f(i)) emit(i) else halt1).repeat

  def take[I](n: Int): Process1[I, I] = if (n <= 0) halt1 else await1[I, I](i => emit(i, take(n - 1)))

  def takeWhile[I](f: I => Boolean): Process1[I, I] = await1(i => if (f(i)) emit(i, takeWhile(f)) else halt1)

  def dropWhile[I](f: I => Boolean): Process1[I, I] = await1(i => if (f(i)) dropWhile(f) else emit(i, id))

  def id[I]: Process1[I, I] = await1((i: I) => emit(i, id))

  /* Emits sep in between each input received. */
  def intersperse[I](sep: I): Process1[I, I] = await1[I, I](i => emit1(i) ++ id.flatMap(i => emit1(sep) ++ emit1(i)))

  type Tee[I, I2, O] = Process[T[I, I2]#f, O]

  def haltT[I, I2, O]: Tee[I, I2, O] = Halt[T[I, I2]#f, O](End)

  def awaitL[I, I2, O](recv: I => Tee[I, I2, O], fallback: => Tee[I, I2, O] = haltT[I, I2, O]): Tee[I, I2, O] =
    await[T[I, I2]#f, I, O](L) {
      case Left(End) => fallback
      case Left(err) => Halt(err)
      case Right(a) => Try(recv(a))
    }

  def awaitR[I, I2, O](recv: I2 => Tee[I, I2, O], fallback: => Tee[I, I2, O] = haltT[I, I2, O]): Tee[I, I2, O] =
    await[T[I, I2]#f, I2, O](R) {
      case Left(End) => fallback
      case Left(err) => Halt(err)
      case Right(a) => Try(recv(a))
    }

  def emitT[I, I2, O](h: O, t1: Tee[I, I2, O] = haltT[I, I2, O]): Tee[I, I2, O] = emit(h, t1)

  def zipWith[I, I2, O](f: (I, I2) => O): Tee[I, I2, O] = awaitL[I, I2, O](i => awaitR(i2 => emitT(f(i, i2)))).repeat

  def zip[I, I2]: Tee[I, I2, (I, I2)] = zipWith((_, _))

  type Sink[F[_], O] = Process[F, O => Process[F, Unit]]

  def fileW(file: String, append: Boolean = false): Sink[IO, String] = resource[FileWriter, String => Process[IO, Unit]] {
    Suspend(Par.lazyUnit(new FileWriter(file, append)))
  } {
    w => constant { (s: String) => eval[IO, Unit](Return(w.write(s))) }
  } {
    w => eval_(Suspend(Par.lazyUnit(w.close())))
  }

  def constant[A](a: A): Process[IO, A] = eval[IO, A](Return(a)).repeat

  // Exercise 15.12

  def join[F[_], A](p: Process[F, Process[F, A]]): Process[F, A] = p.flatMap(pa => pa)

  type Channel[F[_], I, O] = Process[F, I => Process[F, O]]
}
