package com.peknight.demo.cats.effect.standard

import cats.effect.std.{PQueue, PQueueSink, PQueueSource}
import cats.effect.{IO, IOApp}
import cats.implicits.toTraverseOps
import cats.{Contravariant, Functor, Order}

object PriorityQueueApp extends IOApp.Simple:

  val list = List(1, 4, 3, 7, 5, 2, 6, 9, 8)

  given orderForInt: Order[Int] = Order.fromLessThan((x, y) => x < y)

  def absurdlyOverengineeredSort(list: List[Int]) =
    for
      pq <- PQueue.bounded[IO, Int](10)
      _ <- list.traverse(pq.offer)
      l <- List.fill(list.length)(()).traverse(_ => pq.take)
    yield l

  def covariant(list: List[Int]): IO[List[Long]] =
    for
      pq <- PQueue.bounded[IO, Int](10)
      pqOfLongs: PQueueSource[IO, Long] = Functor[[A] =>> PQueueSource[IO, A]].map(pq)(_.toLong)
      _ <- list.traverse(pq.offer)
      l <- List.fill(list.length)(()).traverse(_ => pqOfLongs.take)
    yield l

  def contravariant(list: List[Boolean]): IO[List[Int]] =
    for
      pq <- PQueue.bounded[IO, Int](10)
      pqOfBools: PQueueSink[IO, Boolean] = Contravariant[[A] =>> PQueueSink[IO, A]].contramap[Int, Boolean](pq)(b => if b then 1 else 0)
      _ <- list.traverse(pqOfBools.offer)
      l <- List.fill(list.length)(()).traverse(_ => pq.take)
    yield l

  val run =
    for
      l <- absurdlyOverengineeredSort(list)
      _ <- IO.println(l)
      cl <- covariant(list)
      _ <- IO.println(cl)
      ctl <- contravariant(List(true, false, true, false, false))
      _ <- IO.println(ctl)
    yield ()