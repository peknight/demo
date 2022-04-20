package com.peknight.demo.catseffect.tutorial.producerconsumer

import cats.effect.*
import cats.effect.std.Console
import cats.effect.syntax.all.*
import cats.syntax.all.*

object ProducerConsumerBoundedCancelable extends IOApp:

  //noinspection DuplicatedCode
  def consumer[F[_]: Async: Console](id: Int, stateR: Ref[F, BoundedState[F, Int]]): F[Unit] =
    val take: F[Int] = Deferred[F, Int].flatMap { taker =>
      Async[F].uncancelable { poll =>
        stateR.modify {
          case BoundedState(queue, capacity, takers, offerers) if queue.nonEmpty && offerers.isEmpty =>
            val (i, rest) = queue.dequeue
            BoundedState(rest, capacity, takers, offerers) -> Async[F].pure(i)
          case BoundedState(queue, capacity, takers, offerers) if queue.nonEmpty =>
            val (i, rest) = queue.dequeue
            val ((move, release), tail) = offerers.dequeue
            BoundedState(rest.enqueue(move), capacity, takers, tail) -> release.complete(()).as(i)
          case BoundedState(queue, capacity, takers, offerers) if offerers.nonEmpty =>
            val ((i, release), rest) = offerers.dequeue
            BoundedState(queue, capacity, takers, rest) -> release.complete(()).as(i)
          case BoundedState(queue, capacity, takers, offerers) =>
            val cleanup = stateR.update { s => s.copy(takers = s.takers.filter(_ ne taker)) }
            BoundedState(queue, capacity, takers.enqueue(taker), offerers) -> poll(taker.get).onCancel(cleanup)
        }.flatten
      }
    }
    for
      i <- take
      _ <- if i % 10000 == 0 then Console[F].println(s"Consumer $id has reached $i items") else Async[F].unit
      _ <- consumer(id, stateR)
    yield ()

  //noinspection DuplicatedCode
  def producer[F[_]: Async: Console](id: Int, counterR: Ref[F, Int], stateR: Ref[F, BoundedState[F, Int]]): F[Unit] =
    def offer(i: Int): F[Unit] = Deferred[F, Unit].flatMap[Unit] { offerer =>
      Async[F].uncancelable { poll =>
        stateR.modify {
          case BoundedState(queue, capacity, takers, offerers) if takers.nonEmpty =>
            val (taker, rest) = takers.dequeue
            BoundedState(queue, capacity, rest, offerers) -> taker.complete(i).void
          case BoundedState(queue, capacity, takers, offerers) if queue.size < capacity =>
            BoundedState(queue.enqueue(i), capacity, takers, offerers) -> Async[F].unit
          case BoundedState(queue, capacity, takers, offerers) =>
            val cleanup = stateR.update { s => s.copy(offerers = s.offerers.filter(_._2 ne offerer)) }
            BoundedState(queue, capacity, takers, offerers.enqueue(i -> offerer)) -> poll(offerer.get).onCancel(cleanup)
        }.flatten
      }
    }
    for
      i <- counterR.getAndUpdate(_ + 1)
      _ <- offer(i)
      _ <- if i % 10000 == 0 then Console[F].println(s"Producer $id has reached $i items") else Async[F].unit
      _ <- producer(id, counterR, stateR)
    yield ()

  override def run(args: List[String]): IO[ExitCode] =
    for
      stateR <- Ref.of[IO, BoundedState[IO, Int]](BoundedState.empty[IO, Int](capacity = 100))
      counterR <- Ref.of[IO, Int](1)
      producers = List.range(1, 11).map(producer(_, counterR, stateR)) // 10 producers
      consumers = List.range(1, 11).map(consumer(_, stateR))
      res <- (producers ++ consumers).parSequence.as(ExitCode.Success).handleErrorWith { t =>
        Console[IO].errorln(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
      }
    yield res
