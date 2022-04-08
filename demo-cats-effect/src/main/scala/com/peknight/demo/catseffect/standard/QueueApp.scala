package com.peknight.demo.catseffect.standard

import cats.{Contravariant, Functor}
import cats.effect.{IO, IOApp}
import cats.effect.std.{Queue, QueueSink, QueueSource}
import cats.implicits.toTraverseOps

object QueueApp extends IOApp.Simple {

  def covariant(list: List[Int]): IO[List[Long]] = for {
    q <- Queue.bounded[IO, Int](10)
    qOfLongs: QueueSource[IO, Long] = Functor[QueueSource[IO, *]].map(q)(_.toLong)
    _ <- list.traverse(q.offer)
    l <- List.fill(list.length)(()).traverse(_ => qOfLongs.take)
  } yield l

  def contravariant(list: List[Boolean]): IO[List[Int]] = for {
    q <- Queue.bounded[IO, Int](10)
    qOfBools: QueueSink[IO, Boolean] = Contravariant[QueueSink[IO, *]].contramap[Int, Boolean](q)(b => if (b) 1 else 0)
    _ <- list.traverse(qOfBools.offer)
    l <- List.fill(list.length)(()).traverse(_ => q.take)
  } yield l

  val run = for {
    cl <- covariant(List(1, 2, 3, 4))
    ctl <- contravariant(List(true, false, true, false))
    _ <- IO.println(cl)
    _ <- IO.println(ctl)
  } yield ()
}
