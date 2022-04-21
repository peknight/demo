package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{Callable, CountDownLatch, ExecutorService}
import scala.language.implicitConversions

object Nonblocking:

  sealed trait Future[+A]:
    private[parallelism] def apply(k: A => Unit): Unit

  type Par[+A] = ExecutorService => Future[A]

  object Par:

    def run[A](es: ExecutorService)(p: Par[A]): A =
      val ref = new AtomicReference[A]
      val latch = new CountDownLatch(1)
      p(es) { a => ref.set(a); latch.countDown() }
      latch.await()
      ref.get

    //noinspection DuplicatedCode
    def unit[A](a: A): Par[A] = es => new Future[A]:
      def apply(cb: A => Unit): Unit = cb(a)

    //noinspection DuplicatedCode
    def delay[A](a: => A): Par[A] = es => new Future[A]:
      def apply(cb: A => Unit): Unit = cb(a)

    def fork[A](a: => Par[A]): Par[A] = es => new Future[A]:
      def apply(cb: A => Unit): Unit = eval(es)(a(es)(cb))

    /**
     * Helper function for constructing `Par` values out of calls to non-blocking continuation-passing-style APIs.
     * This will come in handy in Chapter 13.
     */
    def async[A](f: (A => Unit) => Unit): Par[A] = es => new Future[A]:
      def apply(k: A => Unit) = f(k)

    /**
     * Helper function, for evaluating an action
     * asynchronously, using the given `ExecutorService`
     */
    def eval(es: ExecutorService)(r: => Unit): Unit =
      val callable: Callable[Unit] = () => r
      es.submit(callable)

    def map2[A, B, C](p1: Par[A], p2: Par[B])(f: (A, B) => C): Par[C] =
      es => new Future[C]:
        //noinspection DuplicatedCode
        def apply(cb: C => Unit): Unit =
          // Two mutable vars are used to store the two results
          var ar: Option[A] = None
          var br: Option[B] = None

          // An actor that awaits both results, combines them with f, and passes the result to cb
          val combiner = Actor[Either[A, B]](es) {
            // If the A result came in first, stores it in ar and waits for the B.
            // If the A result came last and we already have our B,
            // calls f with both results and passes the resulting C to the callback, cb.
            case Left(a) => br match
              case None => ar = Some(a)
              case Some(b) => eval(es)(cb(f(a, b)))
            // Analogously, if the B result came in first, stores it in br and waits for the A.
            // If the B result came last and we already have our A,
            // calls f with both results and passes the resulting C to the callback, cb.
            case Right(b) => ar match
              case None => br = Some(b)
              case Some(a) => eval(es)(cb(f(a, b)))
          }

          // Passes the actor as a continuation to both sides. On the A side,
          // we wrap the result in Left, and on the B side, we wrap it in Right.
          // These are the constructors of the Either data type,
          // and they serve to indicate to the actor where the result came from.
          p1(es)(a => combiner ! Left(a))
          p2(es)(b => combiner ! Right(b))
        end apply
    end map2


    def mapViaMap2[A, B](pa: Par[A])(f: A => B) = map2(pa, unit(()))((a, _) => f(a))

    def map[A, B](pa: Par[A])(f: A => B): Par[B] = es => new Future[B]:
      def apply(cb: B => Unit): Unit = pa(es)(a => eval(es) { cb(f(a)) })

    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    def asyncF[A, B](f:A => B): A => Par[B] = (a: A) => lazyUnit(f(a))

    def sequenceRight[A](as: List[Par[A]]): Par[List[A]] = as match
      case Nil => unit(Nil)
      case h :: t => map2(h, fork(sequence(t)))(_ :: _)

    //noinspection DuplicatedCode
    def sequenceBalanced[A](as:IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
      if as.isEmpty then unit(Vector())
      else if as.length == 1 then map(as.head)(a => Vector(a))
      else
        val (l, r) = as.splitAt(as.length / 2)
        map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }

    def sequence[A](as: List[Par[A]]): Par[List[A]] = map(sequenceBalanced(as.toIndexedSeq))(_.toList)

    def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] =
      val fbs: List[Par[B]] = ps.map(asyncF(f))
      sequence(fbs)

    // exercise answers
    /*
     * We can implement `choice` as a new primitive.
     *
     * `p(es)(result => ...)` for some `ExecutorServie`, `es`,
     * and some `Par`, `p`, is the idiom for running `p`,
     * and registering a callback to be invoked when its result is available.
     * The result will be bound to `result` in the function passed to `p(es)`.
     *
     * If you find this code difficult to follow, you may want to
     * write down the type of each subexpression and follow the types
     * through the implementation. What is the type of `p(es)`? what
     * abount `t(es)`? What abount `t(es)(cb)`?
     */
    def choice[A](p: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] =
      es => new Future[A]:
        def apply(cb: A => Unit): Unit =
          p(es) { b =>
            if b then eval(es) { t(es)(cb) }
            else eval(es) { f(es)(cb) }
          }

    def choiceN[A](p: Par[Int])(ps: List[Par[A]]): Par[A] = es => new Future[A]:
      def apply(cb: A => Unit): Unit = p(es) { ind => eval(es) { ps(ind)(es)(cb) }}

    def choiceViaChoiceN[A](a: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
      choiceN(map(a)(b => if b then 0 else 1))(List(ifTrue, ifFalse))

    def choiceMap[K,V](p: Par[K])(ps: Map[K,Par[V]]): Par[V] = es => new Future[V]:
      def apply(cb: V => Unit): Unit = p(es)(k => ps(k)(es)(cb))

    /* `chooser` is usually called `flatMap` or `bind`. */
    def chooser[A,B](p: Par[A])(f: A => Par[B]): Par[B] = flatMap(p)(f)

    def flatMap[A,B](p: Par[A])(f: A => Par[B]): Par[B] = es => new Future[B]:
      def apply(cb: B => Unit): Unit = p(es)(a => f(a)(es)(cb))

    def choiceViaFlatMap[A](p: Par[Boolean])(f: Par[A], t: Par[A]): Par[A] =
      flatMap(p)(b => if b then t else f)

    def choiceNViaFlatMap[A](p: Par[Int])(choices: List[Par[A]]): Par[A] =
      flatMap(p)(i => choices(i))

    def join[A](p: Par[Par[A]]): Par[A] = es => new Future[A]:
      def apply(cb: A => Unit): Unit = p(es)(p2 => eval(es) { p2(es)(cb) })

    def joinViaFlatMap[A](a: Par[Par[A]]): Par[A] = flatMap(a)(x => x)

    def flatMapViaJoin[A,B](p: Par[A])(f: A => Par[B]): Par[B] = join(map(p)(f))

  end Par

  extension [A](p: Par[A])
    def map[B](f: A => B): Par[B] = Par.map(p)(f)
    def map2[B,C](b: Par[B])(f: (A,B) => C): Par[C] = Par.map2(p,b)(f)
    def flatMap[B](f: A => Par[B]): Par[B] = Par.flatMap(p)(f)
    def zip[B](b: Par[B]): Par[(A,B)] = p.map2(b)((_,_))

end Nonblocking
