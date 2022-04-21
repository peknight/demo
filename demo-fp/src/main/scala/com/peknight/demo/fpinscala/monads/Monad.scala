package com.peknight.demo.fpinscala.monads

import com.peknight.demo.fpinscala.applicative.{Applicative, Traverse}
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
import com.peknight.demo.fpinscala.parsing.Parsers
import com.peknight.demo.fpinscala.state.State
import com.peknight.demo.fpinscala.testing.Gen

import scala.language.implicitConversions

trait Monad[F[_]] extends Applicative[F]:

  def unit[A](a: => A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = join(map(fa)(f))

  override def map[A, B](ma: F[A])(f: A => B): F[B] = flatMap(ma)(a => unit(f(a)))
  override def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = flatMap(fa)(a => map(fb)(b => f(a, b)))

  // Exercise 11.3

  override def sequence[A](lma: List[F[A]]): F[List[A]] = lma.foldRight(unit(List.empty[A])) { (fa, acc) =>
    map2(fa, acc)(_ :: _)
  }

  override def traverse[A, B](la: List[A])(f: A => F[B]): F[List[B]] = la.foldRight(unit(List.empty[B])) { (a, acc) =>
    map2(f(a), acc)(_ :: _)
  }

  /**
   * 'Balanced' sequencing, which should behave like `sequecne`,
   * but it can use less stack for some data types. We'll see later
   * in this chapter how the monad _laws_ let us conclude both
   * definitions 'mean' the same thing.
   */
  def bsequence[A](ms: Seq[F[A]]): F[IndexedSeq[A]] =
    if ms.isEmpty then unit(Vector.empty[A])
    else if ms.size == 1 then map(ms.head)(Vector(_))
    else
      val (l, r) = ms.toIndexedSeq.splitAt(ms.length / 2)
      map2(bsequence(l), bsequence(r))(_ ++ _)

  // Exercise 11.4

  def _replicateM[A](n: Int, ma: F[A]): F[List[A]] =
    if n <= 0 then unit(List.empty[A])
    else map2(ma, _replicateM(n - 1, ma))(_ :: _)

  // See `Gen` and `Parser`'s `listOfN`. `replicateM` meaning "replicate in a monad"
  // override def replicateM[A](n: Int, ma: F[A]): F[List[A]] = sequence(List.fill(n)(ma))
  override def replicateM[A](n: Int)(f: F[A]): F[List[A]] =
    LazyList.fill(n)(f).foldRight(unit(List.empty[A]))(map2(_, _)(_ :: _))

  def replicateM_[A](n: Int)(f: F[A]): F[Unit] = foreachM(LazyList.fill(n)(f))(skip)

  // Exercise 11.6

  def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] = ms.foldRight(unit(List.empty[A])){ (a, acc) =>
    map2(f(a), acc)((flag, list) => if flag then a :: list else list)
  }

  def _filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] = ms match
    case Nil => unit(Nil)
    case h :: t => flatMap(f(h))(b => if !b then _filterM(t)(f) else map(filterM(t)(f))(h :: _))

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => flatMap(f(a))(g)

  // Exercise 11.8
  def flatMapViaCompose[A, B](fa: F[A])(f: A => F[B]): F[B] = compose((_: Unit) => fa, f)(())

  // Exercise 11.12

  // Join is sometimes called "flatten", and `flatMap` "maps and then flattens".
  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(identity)

  // Exercise 11.13

  def flatMapViaJoin[A, B](fa: F[A])(f: A => F[B]): F[B] = join(map(fa)(f))

  def composeViaJoin[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => join(map(f(a))(g))

  def sequence_[A](fs: LazyList[F[A]]): F[Unit] = foreachM(fs)(skip)
  def sequence_[A](fs: F[A]*): F[Unit] = sequence_(fs.to(LazyList))
  def as[A, B](a: F[A])(b: B): F[B] = map(a)(_ => b)
  def skip[A](fa: F[A]): F[Unit] = as(fa)(())
  def when[A](b: Boolean)(fa: => F[A]): F[Boolean] = if b then as(fa)(true) else unit(false)
  def while_(a: F[Boolean])(b: F[Unit]): F[Unit] =
    lazy val t: F[Unit] = while_(a)(b)
    flatMap(a)(c => skip(when(c)(t)))

  def doWhile[A](fa: F[A])(cond: A => F[Boolean]): F[Unit] =
    for
      a <- fa
      ok <- cond(a)
      _ <- if ok then doWhile(fa)(cond) else unit(())
    yield ()

  def forever[A, B](a: F[A]): F[B] =
    // lazy val t: F[B] = forever(a)
    // flatMap(a)(_ => t)
    lazy val t: F[B] = flatMap(a)(_ => t)
    t

  def foldM[A, B](l: LazyList[A])(z: B)(f: (B, A) => F[B]): F[B] = l match
    case h #:: t => flatMap(f(z, h))(z2 => foldM(t)(z2)(f))
    case _ => unit(z)

  def foldM_[A, B](l: LazyList[A])(z: B)(f: (B, A) => F[B]): F[Unit] = skip { foldM(l)(z)(f) }

  def foreachM[A](l: LazyList[A])(f: A => F[Unit]): F[Unit] = foldM_(l)(())((_, a) => skip(f(a)))

  def seq[A, B, C](f: A => F[B])(g: B => F[C]): A => F[C] = f andThen (fb => flatMap(fb)(g))

  // syntax
  given toMonadic[A]: Conversion[F[A], Monadic[F, A]] = (a: F[A]) => new Monadic[F, A]:
    val F = Monad.this
    def get = a

end Monad

object Monad:

  val genMonad = new Monad[Gen]:
    def unit[A](a: => A): Gen[A] = Gen.unit(a)
    override def flatMap[A, B](ma: Gen[A])(f: A => Gen[B]): Gen[B] = ma flatMap f

  // Exercise 11.1

  given parMonad: Monad[Par] with
    def unit[A](a: => A): Par[A] = Par.unit(a)
    override def flatMap[A, B](ma: Par[A])(f: A => Par[B]): Par[B] = Par.flatMap(ma)(f)

  def parserMonad[Parser[+_]](p: Parsers[Parser]): Monad[Parser] = new Monad:
    def unit[A](a: => A): Parser[A] = p.succeed(a)
    override def flatMap[A, B](fa: Parser[A])(f: A => Parser[B]): Parser[B] = p.flatMap(fa)(f)

  val optionMonad = new Monad[Option]:
    def unit[A](a: => A): Option[A] = Option(a)
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa flatMap f

  val lazyListMonad = new Monad[LazyList]:
    def unit[A](a: => A): LazyList[A] = LazyList(a)
    override def flatMap[A, B](fa: LazyList[A])(f: A => LazyList[B]): LazyList[B] = fa flatMap f

  val listMonad = new Monad[List]:
    def unit[A](a: => A): List[A] = List(a)
    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa flatMap f

  // Exercise 11.2

  class StateMonads[S]:
    type StateS[A] = State[S, A]

     val monad = new Monad[StateS]:
       def unit[A](a: => A): State[S, A] = State.unit(a)
       override def flatMap[A, B](st: State[S, A])(f: A => State[S, B]): State[S, B] = st flatMap f
  end StateMonads

  // type lambda kind projector: Monad[State[S, *]]
  def stateMonad[S] = new Monad[[T] =>> State[S, T]]:
    def unit[A](a: => A): State[S, A] = State.unit(a)
    override def flatMap[A, B](st: State[S, A])(f: A => State[S, B]): State[S, B] = st flatMap f

  // Exercise 12.20

  def composeM[G[_], H[_]](using G: Monad[G], H: Monad[H], T: Traverse[H]): Monad[[T] =>> G[H[T]]] = new Monad:
    def unit[A](a: => A): G[H[A]] = G.unit(H.unit(a))
    override def flatMap[A, B](mna: G[H[A]])(f: A => G[H[B]]): G[H[B]] =
      G.flatMap(mna)(na => G.map(T.traverse(na)(f))(H.join))

  case class OptionT[M[_], A](value: M[Option[A]])(using M: Monad[M]):
    def flatMap[B](f: A => OptionT[M, B]): OptionT[M, B] = OptionT(M.flatMap(value) {
      case None => M.unit(None)
      case Some(a) => f(a).value
    })