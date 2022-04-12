package com.peknight.demo.catseffect.standard

import cats.effect.std.{Dequeue, DequeueSink, DequeueSource}
import cats.effect.{IO, IOApp}
import cats.implicits.toTraverseOps
import cats.{Contravariant, Functor}

/**
 * 双端队列，可以用Functor和Contravariant作为上/下游封装
 */
object DequeueApp extends IOApp.Simple {

  def covariant(list: List[Int]): IO[List[Long]] = for {
    q <- Dequeue.bounded[IO, Int](10)
    // kind projector
    qOfLongs: DequeueSource[IO, Long] = Functor[DequeueSource[IO, *]].map(q)(_.toLong)
    _ <- list.traverse(q.offer)
    l <- List.fill(list.length)(()).traverse(_ => qOfLongs.take)
  } yield l

  def contravariant(list: List[Boolean]): IO[List[Int]] = for {
    q <- Dequeue.bounded[IO, Int](10)
    qOfBools: DequeueSink[IO, Boolean] = Contravariant[DequeueSink[IO, *]].contramap[Int, Boolean](q)(b => if (b) 1 else 0)
    _ <- list.traverse(qOfBools.offer)
    l <- List.fill(list.length)(()).traverse(_ => q.take)
  } yield l

  val run = for {
    _ <- covariant(List(1, 4, 2, 3, 5, 6, 9, 8, 7, 0)).flatMap(IO.println)
    _ <- contravariant(List(true, false)).flatMap(IO.println)
  } yield ()
}
