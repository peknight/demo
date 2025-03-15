package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.*
import cats.effect.std.Console
import cats.syntax.all.*

object ProducerConsumer extends IOApp:

  def consumer[F[_]: {Async, Console}](id: Int, stateR: Ref[F, State[F, Int]]): F[Unit] =
    val take: F[Int] = Deferred[F, Int].flatMap { taker =>
      stateR.modify {
        case State(queue, takers) if queue.nonEmpty =>
          val (i, rest) = queue.dequeue
          State(rest, takers) -> Async[F].pure(i)
        case State(queue, takers) => State(queue, takers.enqueue(taker)) -> taker.get
      }.flatten
    }
    for
      i <- take
      _ <- if i % 10000 == 0 then Console[F].println(s"Consumer $id has reached $i times") else Async[F].unit
      _ <- consumer(id, stateR)
    yield ()

  def producer[F[_]: {Sync, Console}](id: Int, counterR: Ref[F, Int], stateR: Ref[F, State[F, Int]]): F[Unit] =
    def offer(i: Int): F[Unit] = stateR.modify {
      case State(queue, takers) if takers.nonEmpty =>
        val (taker, rest) = takers.dequeue
        State(queue, rest) -> taker.complete(i).void
      case State(queue, takers) => State(queue.enqueue(i), takers) -> Sync[F].unit
    }.flatten
    for
      i <- counterR.getAndUpdate(_ + 1)
      _ <- offer(i)
      _ <- if i % 10000 == 0 then Console[F].println(s"Producer $id has reached $i items") else Sync[F].unit
      _ <- producer(id, counterR, stateR)
    yield ()

  //noinspection DuplicatedCode
  override def run(args: List[String]): IO[ExitCode] =
    for
      stateR <- Ref.of[IO, State[IO, Int]](State.empty[IO, Int])
      counterR <- Ref.of[IO, Int](1)
      producers = List.range(1, 11).map(producer(_, counterR, stateR))
      consumers = List.range(1, 11).map(consumer(_, stateR))
      res <- (producers ++ consumers).parSequence.as(ExitCode.Success).handleErrorWith { t =>
        Console[IO].errorln(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
      }
    yield res
