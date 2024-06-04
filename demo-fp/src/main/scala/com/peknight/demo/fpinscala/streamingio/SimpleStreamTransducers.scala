package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.iomonad.App.IO
import com.peknight.demo.fpinscala.iomonad.Free.Suspend
import com.peknight.demo.fpinscala.monads.Monad.given
import com.peknight.demo.fpinscala.monads.{Monad, Monadic}
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
import com.peknight.demo.fpinscala.streamingio.SimpleStreamTransducers.Process.*

import scala.annotation.tailrec
import scala.language.implicitConversions

object SimpleStreamTransducers:

  /**
   * 一个简单的流转换器
   */
  sealed trait Process[I, O]:

    def apply(s: LazyList[I]): LazyList[O] = this match
      case Halt() => LazyList()
      case Await(recv) => s match
        case h #:: t => recv(Some(h))(t)
        case xs => recv(None)(xs)
      case Emit(h, t) => h #:: t(s)

    // repeat组合子会无限递归，而Emit又是立即求值的，所以我们绝对不能对非等待的Process执行这样的操作
    def repeat: Process[I, O] =
      def go(p: Process[I, O]): Process[I, O] = p match
        // 结束的时候从头来
        case Halt() => go(this)
        // Await的时候继续转换
        case Await(recv) =>
          Await {
            // 当源截止时停止循环
            case None => recv(None)
            case i => go(recv(i))
          }
        // Emit时继续转换
        case Emit(h, t) => Emit(h, go(t))
      go(this)

    def repeatN(n: Int): Process[I, O] =
      def go(n: Int, p: Process[I, O]): Process[I, O] = p match
        case Halt() => if n > 0 then go(n - 1, this) else Halt()
        case Await(recv) =>
          Await {
            case None => recv(None)
            case i => go(n, recv(i))
          }
        case Emit(h, t) => Emit(h, go(n, t))
      go(n, this)

    // Exercise 15.5

    def |>[O2](p2: Process[O, O2]): Process[I, O2] = p2 match
      case Halt() => Halt()
      case Emit(head, tail) => Emit(head, this |> tail)
      case Await(f) => this match
        case Emit(head, tail) => tail |> f(Some(head))
        case Halt() => this |> f(None)
        case Await(g) => Await { (i: Option[I]) => g(i) |> p2 }

    def mapViaLift[O2](f: O => O2): Process[I, O2] = this |> lift(f)

    def map[O2](f: O => O2): Process[I, O2] = this match
      case Halt() => Halt[I, O2]()
      case Emit(h, t) => Emit(f(h), t.map(f))
      case Await(recv) => Await(recv.andThen(_.map(f)))

    def ++(p: => Process[I, O]): Process[I, O] = this match
      case Halt() => p
      case Emit(h, t) => Emit(h, t ++ p)
      case Await(recv) => Await(recv.andThen(_ ++ p))

    def flatMap[O2](f: O => Process[I, O2]): Process[I, O2] = this match
      case Halt() => Halt[I, O2]()
      case Emit(h, t) => f(h) ++ t.flatMap(f)
      case Await(recv) => Await(recv.andThen(_.flatMap(f)))

    // Exercise 15.6
    def zipWithIndex: Process[I, (O, Int)] = zip(count.map(_ - 1))

    /*
     * Feed `in` to this `Process`. Uses a tail recursive loop as long
     * as `this` is in the `Await` state.
     */
    def feed(in: Seq[I]): Process[I, O] =
      def go(in: Seq[I], cur: Process[I, O]): Process[I, O] = cur match
        case Halt() => Halt()
        case Await(recv) => if in.nonEmpty then go(in.tail, recv(Some(in.head))) else cur
        case Emit(h, t) => Emit(h, t.feed(in))
      go(in, this)

    def filter(f: O => Boolean): Process[I, O] = this |> Process.filter(f)

    def zip[O2](p: Process[I, O2]): Process[I, (O, O2)] = Process.zip(this, p)

    /* Add `p` to the fallback branch of this process */
    def orElse(p: Process[I, O]): Process[I, O] = this match
      case Halt() => p
      case Await(recv) =>
        Await {
          case None => p
          case x => recv(x)
        }
      case _ => this

  end Process

  object Process:

    // 告诉驱动器将head值传递给输出流，而后tail的部分继续由状态机处理
    case class Emit[I, O](head: O, tail: Process[I, O] = Halt[I, O]()) extends Process[I, O]
    // 请求从输入流得到下一个值，驱动器则将下一个值传递给函数recv，一旦输入流再无元素则给None
    case class Await[I, O](recv: Option[I] => Process[I, O]) extends Process[I, O]
    // 告诉驱动器暂没有任何元素要从输入流里读取，或递送给输出流；不用协逆变就不能把这个指定成object
    case class Halt[I, O]() extends Process[I, O]

    def emit[I, O](head: O, tail: Process[I, O] = Halt[I, O]()): Process[I, O] = Emit(head, tail)

    def await[I, O](f: I => Process[I, O], fallback: Process[I, O] = Halt[I, O]()): Process[I, O] = Await {
      case Some(i) => f(i)
      case None => fallback
    }

    def liftOne[I, O](f: I => O): Process[I, O] = Await {
      case Some(i) => Emit(f(i))
      case None => Halt()
    }

    def lift[I, O](f: I => O): Process[I, O] = liftOne(f).repeat

    /* The identity `Process`, just repeatedly echos its input. */
    def id[I]: Process[I, I] = lift(identity)

    def filter[I](p: I => Boolean): Process[I, I] = Await[I, I] {
      case Some(i) if p(i) => emit(i)
      case _ => Halt()
    }.repeat

    def sumOrigin: Process[Double, Double] =
      def go(acc: Double): Process[Double, Double] = Await {
        case Some(d) => Emit(d + acc, go(d + acc))
        case None => Halt()
      }
      go(0.0)

    def sum: Process[Double, Double] = loop(0.0)((d: Double, sum: Double) => (sum + d, sum + d))

    // Exercise 15.1

    def take[I](n: Int): Process[I, I] = if n <= 0 then Halt() else await(i => emit(i, take(n - 1)))

    def drop[I](n: Int): Process[I, I] = if n <= 0 then id else await(i => drop[I](n - 1))

    def takeWhile[I](f: I => Boolean): Process[I, I] = await { i => if f(i) then emit(i, takeWhile(f)) else Halt() }

    def dropWhile[I](f: I => Boolean): Process[I, I] = await { i => if f(i) then dropWhile(f) else id }

    // Exercise 15.2

    def count[I]: Process[I, Int] = lift((i: I) => 1.0) |> sum |> lift(_.toInt)

    def count2[I]: Process[I, Int] =
      def go(acc: Int): Process[I, Int] = await { (_: I) => emit(acc + 1, go(acc + 1)) }
      go(0)

    def countViaLoop[I]: Process[I, Int] = loop(0)((i: I, s: Int) => (s + 1, s + 1))

    // Exercise 15.3

    def mean: Process[Double, Double] =
      def go(sum: Double, count: Int): Process[Double, Double] = await {
        (d: Double) => emit((sum + d) / (count + 1), go(sum + d, count + 1))
      }
      go(0.0, 0)

    def meanViaLoop: Process[Double, Double] = loop((0.0, 0)) {
      case (d: Double, (sum: Double, count: Int)) => ((sum + d) / (count + 1), (sum + d, count + 1))
    }

    def mean2 = sum.zip(count) |> lift { case (s, n) => s / n }

    def loop[S, I, O](z: S)(f: (I, S) => (O, S)): Process[I, O] = await { (i: I) =>
      f(i, z) match
        case (o, s2) => emit(o, loop(s2)(f))
    }

    def monad[I]: Monad[[A] =>> Process[I, A]] = new Monad[[A] =>> Process[I, A]]:
      def unit[O](o: => O): Process[I, O] = Emit(o)
      override def flatMap[O, O2](p: Process[I, O])(f: O => Process[I, O2]): Process[I, O2] = p.flatMap(f)

    given toMonadic[I, O]: Conversion[Process[I, O], Monadic[[A] =>> Process[I, A], O]] = monad[I].toMonadic(_)

    // Exercise 15.7
    def zip[A, B, C](p1: Process[A, B], p2: Process[A, C]): Process[A, (B, C)] = (p1, p2) match
      case (Halt(), _) => Halt[A, (B, C)]()
      case (_, Halt()) => Halt[A, (B, C)]()
      case (Emit(b, t1), Emit(c, t2)) => Emit((b, c), zip(t1, t2))
      case (Await(recv1), _) => Await((oa: Option[A]) => zip(recv1(oa), feed(oa)(p2)))
      case (_, Await(recv2)) => Await((oa: Option[A]) => zip(feed(oa)(p1), recv2(oa)))

    def feed[A, B](oa: Option[A])(p: Process[A, B]): Process[A, B] = p match
      case Halt() => p
      case Emit(h, t) => Emit(h, feed(oa)(t))
      case Await(recv) => recv(oa)

    // Exercise 15.8

    /* Emit all intermediate values, and not halt. */
    def exists[I](f: I => Boolean): Process[I, Boolean] = lift(f) |> any

    /* Emits whether a `true` input has ever been received. */
    def any: Process[Boolean, Boolean] = loop(false)((b: Boolean, s: Boolean) => (s || b, s || b))

    /* A trimmed `exists`, containing just the final result. */
    def existsResult[I](f: I => Boolean): Process[I, Boolean] =
      exists(f) |> takeThrough(!_) |> dropWhile(!_) |> echo.orElse(emit(false))

    /* Like `takeWhile`, but includes the first element that tests false. */
    def takeThrough[I](f: I => Boolean): Process[I, I] = takeWhile(f) ++ echo

    /* Awaits then emits a single value, then halts */
    def echo[I]: Process[I, I] = await(i => emit(i))

    def processFile[A, B](f: java.io.File, p: Process[String, A], z: B)(g: (B, A) => B): IO[B] = Suspend(Par.lazyUnit {
      @tailrec def go(ss: Iterator[String], cur: Process[String, A], acc: B): B = cur match
        case Halt() => acc
        case Await(recv) =>
          val next = if ss.hasNext then recv(Some(ss.next())) else recv(None)
          go(ss, next, acc)
        case Emit(h, t) => go(ss, t, g(acc, h))
      val s = io.Source.fromFile(f)
      try go(s.getLines(), p, z) finally s.close()
    })

    // Exercise 15.9

    def toCelsius(fahrenheit: Double): Double = (5.0 / 9.0) * (fahrenheit - 32.0)

    def convertFahrenheit: Process[String, Double] =
      filter((line: String) => !line.startsWith("#")) |>
        filter(line => line.trim.nonEmpty) |>
        lift(line => toCelsius(line.toDouble))

    def skip[I, O]: Process[I, O] = await(i => Halt())
    def ignore[I, O]: Process[I, O] = skip.repeat

    def terminated[I]: Process[I, Option[I]] = await((i: I) => emit(Some(i), terminated[I]), emit(None))

  end Process
end SimpleStreamTransducers
