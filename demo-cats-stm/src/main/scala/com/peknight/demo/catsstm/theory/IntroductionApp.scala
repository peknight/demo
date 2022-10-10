package com.peknight.demo.catsstm.theory

import cats.effect.IO

object IntroductionApp:
  def transfer(from: Account, to: Account, amount: Long): Unit =
    // Acquire an exclusive lock on from
    from.synchronized {
      // Acquire an exclusive lock on to
      to.synchronized {
        from.modify(_ - amount)
        to.modify(_ + amount)
      }
    }

  def foo: Unit = ???
  def bar: Unit = ???

  trait STM[F[_]]:
    trait TVar[A]:
      def get: Txn[A]
      def modify(f: A => A): Txn[Unit]
      def set(a: A): Txn[Unit]
    object TVar:
      def of[A](a: A): Txn[TVar[A]] = ???
    trait Txn[A]:
      def flatMap[B](f: A => Txn[B]): Txn[B]
      def map[B](f: A => B): Txn[B]
    def commit[A](txn: Txn[A]): F[A]
  end STM

  def example(stm: STM[IO]): IO[Unit] =
    import stm.*
    for
      from <- stm.commit(TVar.of(100))
      to <- stm.commit(TVar.of(0))
      _ <- stm.commit(
        for
          curr <- from.get
          _ <- from.set(0)
          _ <- to.set(curr)
        yield ()
      )
    yield ()
