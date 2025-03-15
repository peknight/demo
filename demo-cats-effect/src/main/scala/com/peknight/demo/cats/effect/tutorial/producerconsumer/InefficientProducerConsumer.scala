package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.*
import cats.effect.std.Console
import cats.syntax.all.*

import scala.collection.immutable.Queue

object InefficientProducerConsumer extends IOApp:

  def producer[F[_]: {Sync, Console}](queueR: Ref[F, Queue[Int]], counter: Int): F[Unit] =
    for
      _ <- if counter % 10000 == 0 then Console[F].println(s"Produced $counter items") else Sync[F].unit
      _ <- queueR.getAndUpdate(_.enqueue(counter + 1))
      _ <- producer(queueR, counter + 1)
    yield ()

  def consumer[F[_]: {Sync, Console}](queueR: Ref[F, Queue[Int]]): F[Unit] =
    for
      iO <- queueR.modify { queue => queue.dequeueOption.fold((queue, Option.empty[Int])) {
        case (i, queue) => (queue, Option(i))
      } }
      _ <- if iO.exists(_ % 10000 == 0) then Console[F].println(s"Consumed ${iO.get} items") else Sync[F].unit
      _ <- consumer(queueR)
    yield ()

  //noinspection DuplicatedCode
  /*
   * parMapN可以抛出错误并自动cancel相关fiber，更好推断
   */
  val version1: IO[ExitCode] =
    for
      queueR <- Ref.of[IO, Queue[Int]](Queue.empty[Int])
      res <- (consumer(queueR), producer(queueR, 0)).parMapN((_, _) => ExitCode.Success).handleErrorWith {t =>
        Console[IO].errorln(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
      }
    yield res

  /*
   * 不推荐直接操作底层fiber的这种方式（用start、join）
   * 如果fiber运行中出现错误，join不会抛出错误，需要手动检查返回的Outcome判断是否出错，其它fiber并不会感知出错
   */
  val version2: IO[ExitCode] =
    for
      queueR <- Ref.of[IO, Queue[Int]](Queue.empty[Int])
      consumerFiber <- consumer(queueR).start
      producerFiber <- producer(queueR, 0).start
      _ <- producerFiber.join
      _ <- consumerFiber.join
    yield ExitCode.Error

  override def run(args: List[String]): IO[ExitCode] = version2
