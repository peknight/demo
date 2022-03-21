package com.peknight.demo.fpinscala.monads

import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
import com.peknight.demo.fpinscala.parsing.Parsers
import com.peknight.demo.fpinscala.state.State
import com.peknight.demo.fpinscala.testing.Gen

trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def map[A, B](ma: F[A])(f: A => B): F[B] = flatMap(ma)(a => unit(f(a)))
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = flatMap(fa)(a => map(fb)(b => f(a, b)))

  // Exercise 11.3

  def sequence[A](lma: List[F[A]]): F[List[A]] = lma.foldRight(unit(List.empty[A])) { (fa, acc) =>
    map2(fa, acc)(_ :: _)
  }

  def traverse[A, B](la: List[A])(f: A => F[B]): F[List[B]] = la.foldRight(unit(List.empty[B])) { (a, acc) =>
    map2(f(a), acc)(_ :: _)
  }

  /**
   * 'Balanced' sequencing, which should behave like `sequecne`,
   * but it can use less stack for some data types. We'll see later
   * in this chapter how the monad _laws_ let us conclude both
   * definitions 'mean' the same thing.
   */
  def bsequence[A](ms: Seq[F[A]]): F[IndexedSeq[A]] = {
    if (ms.isEmpty) unit(Vector.empty[A])
    else if (ms.size == 1) map(ms.head)(Vector(_))
    else {
      val (l, r) = ms.toIndexedSeq.splitAt(ms.length / 2)
      map2(bsequence(l), bsequence(r))(_ ++ _)
    }
  }

  // Exercise 11.4

  def _replicateM[A](n: Int, ma: F[A]): F[List[A]] =
    if (n <= 0) unit(List.empty[A])
    else map2(ma, _replicateM(n - 1, ma))(_ :: _)

  // See `Gen` and `Parser`'s `listOfN`. `replicateM` meaning "replicate in a monad"
  def replicateM[A](n: Int, ma: F[A]): F[List[A]] = sequence(List.fill(n)(ma))

  // Exercise 11.6

  def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] = ms.foldRight(unit(List.empty[A])){ (a, acc) =>
    map2(f(a), acc)((flag, list) => if (flag) a :: list else list)
  }

  def _filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] = ms match {
    case Nil => unit(Nil)
    case h :: t => flatMap(f(h))(b => if (!b) _filterM(t)(f) else map(filterM(t)(f))(h :: _))
  }
}

object Monad {

  val genMonad = new Monad[Gen] {
    def unit[A](a: => A): Gen[A] = Gen.unit(a)
    def flatMap[A, B](ma: Gen[A])(f: A => Gen[B]): Gen[B] = ma flatMap f
  }

  // Exercise 11.1

  val parMonad = new Monad[Par] {
    def unit[A](a: => A): Par[A] = Par.unit(a)
    def flatMap[A, B](ma: Par[A])(f: A => Par[B]): Par[B] = Par.flatMap(ma)(f)
  }

  def parserMonad[Parser[+_]](p: Parsers[Parser]): Monad[Parser] = new Monad[Parser] {
    def unit[A](a: => A): Parser[A] = p.succeed(a)
    def flatMap[A, B](fa: Parser[A])(f: A => Parser[B]): Parser[B] = p.flatMap(fa)(f)
  }

  val optionMonad = new Monad[Option] {
    def unit[A](a: => A): Option[A] = Option(a)
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa flatMap f
  }

  val lazyListMonad = new Monad[LazyList] {
    def unit[A](a: => A): LazyList[A] = LazyList(a)
    def flatMap[A, B](fa: LazyList[A])(f: A => LazyList[B]): LazyList[B] = fa flatMap f
  }

  val listMonad = new Monad[List] {
    def unit[A](a: => A): List[A] = List(a)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa flatMap f
  }

  // Exercise 11.2

  class StateMonads[S] {
    type StateS[A] = State[S, A]

     val monad = new Monad[StateS] {
       def unit[A](a: => A): State[S, A] = State.unit(a)
       def flatMap[A, B](st: State[S, A])(f: A => State[S, B]): State[S, B] = st flatMap f
     }
  }

  // type lambda kind projector: Monad[State[S, *]]
  def stateMonad[S] = new Monad[({type f[x] = State[S, x]})#f] {
    def unit[A](a: => A): State[S, A] = State.unit(a)
    def flatMap[A, B](st: State[S, A])(f: A => State[S, B]): State[S, B] = st flatMap f
  }
}
