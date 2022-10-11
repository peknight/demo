package com.peknight.demo.catsstm.examples

import cats.data.NonEmptyList
import cats.effect.std.Random
import cats.effect.{FiberIO, IO, IOApp}
import cats.syntax.eq.*
import cats.syntax.foldable.*
import cats.syntax.show.*
import io.github.timwspence.cats.stm.*

import scala.concurrent.duration.*

object TheSantaClausProblemApp extends IOApp.Simple:

  class CatsSTM(val stm: STM[IO]):
    import stm.*

    def meetInStudy(id: Int): IO[Unit] = IO.println(show"Elf $id meeting in the study")
    def deliverToys(id: Int): IO[Unit] = IO.println(show"Reindeer $id delivering toys")

    sealed abstract case class Gate(capacity: Int, tv: TVar[Int]):
      def pass: IO[Unit] = Gate.pass(this)
      def operate: IO[Unit] = Gate.operate(this)
    object Gate:
      def of(capacity: Int): Txn[Gate] = TVar.of(0).map(new Gate(capacity, _){})
      def pass(g: Gate): IO[Unit] = stm.commit {
        for
          nLeft <- g.tv.get
          _ <- stm.check(nLeft > 0)
          _ <- g.tv.modify(_ - 1)
        yield ()
      }
      def operate(g: Gate): IO[Unit] =
        for
          _ <- stm.commit(g.tv.set(g.capacity))
          _ <- stm.commit {
            for
              nLeft <- g.tv.get
              _ <- stm.check(nLeft === 0)
            yield ()
          }
        yield ()
    end Gate

    sealed abstract case class Group(n: Int, tv: TVar[(Int, Gate, Gate)]):
      def join: IO[(Gate, Gate)] = Group.join(this)
      def await: Txn[(Gate, Gate)] = Group.await(this)
    object Group:
      def of(n: Int): IO[Group] = stm.commit {
        for
          g1 <- Gate.of(n)
          g2 <- Gate.of(n)
          tv <- TVar.of((n, g1, g2))
        yield new Group(n, tv){}
      }
      def join(g: Group): IO[(Gate, Gate)] = stm.commit {
        for
          value <- g.tv.get
          (nLeft, g1, g2) = value
          _ <- stm.check(nLeft > 0)
          _ <- g.tv.set((nLeft - 1, g1, g2))
        yield (g1, g2)
      }
      def await(g: Group): Txn[(Gate, Gate)] =
        for
          value <- g.tv.get
          (nLeft, g1, g2) = value
          _ <- stm.check(nLeft === 0)
          newG1 <- Gate.of(g.n)
          newG2 <- Gate.of(g.n)
          _ <- g.tv.set((g.n, newG1, newG2))
        yield (g1, g2)
    end Group

    def helper1(group: Group, doTask: IO[Unit]): IO[Unit] =
      for
        value <- group.join
        (inGate, outGate) = value
        _ <- inGate.pass
        _ <- doTask
        _ <- outGate.pass
      yield ()

    def elf2(group: Group, id: Int): IO[Unit] = helper1(group, meetInStudy(id))

    def reindeer2(group: Group, id: Int): IO[Unit] = helper1(group, deliverToys(id))

    def randomDelay: IO[Unit] =
      for
        random <- Random.scalaUtilRandom[IO]
        rng <- random.nextIntBounded(10000)
        _ <- IO.sleep(rng.micros)
      yield ()

    def elf(g: Group, i: Int): IO[FiberIO[Nothing]] = (elf2(g, i) >> randomDelay).foreverM.start

    def reindeer(g: Group, i: Int): IO[FiberIO[Nothing]] = (reindeer2(g, i) >> randomDelay).foreverM.start

    def choose[A](choices: NonEmptyList[(Txn[A], A => IO[Unit])]): IO[Unit] =
      def actions: NonEmptyList[Txn[IO[Unit]]] = choices.map {
        case (guard, rhs) =>
          for
            value <- guard
          yield rhs(value)
      }
      for
        act <- stm.commit(actions.reduceLeft(_.orElse(_)))
        _ <- act
      yield ()
    end choose

    def santa(elfGroup: Group, reinGroup: Group): IO[Unit] =
      def run(task: String, gates: (Gate, Gate)): IO[Unit] =
        for
          _ <- IO.println(show"Ho! Ho! Ho! let's $task")
          _ <- gates._1.operate
          _ <- gates._2.operate
        yield ()
      for
        _ <- IO.println("----------")
        _ <- choose[(Gate, Gate)](NonEmptyList.of(
          (reinGroup.await, (g: (Gate, Gate)) => run("deliver toys", g)),
          (elfGroup.await, (g: (Gate, Gate)) => run("meet in study", g))
        ))
      yield ()
    end santa

    def main: IO[Unit] =
      for
        elfGroup <- Group.of(3)
        _ <- (1 to 10).toList.traverse_(n => elf(elfGroup, n))
        reinGroup <- Group.of(9)
        _ <- (1 to 9).toList.traverse_(n => reindeer(reinGroup, n))
        _ <- santa(elfGroup, reinGroup).foreverM.void
      yield ()

  end CatsSTM

  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      catsStm = CatsSTM(stm)
      res <- catsStm.main.timeout(2.seconds).attempt
      _ <- IO.println(res)
    yield ()
