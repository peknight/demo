package com.peknight.demo.playground.fs2.pull

import cats.effect.{IO, IOApp}
import fs2.{Chunk, INothing, Pull, Pure, Stream}

object ChunksApp extends IOApp.Simple:

  def stream[F[_]]: Stream[F, Int] = Stream.chunk(Chunk.apply(1, 2, 3))
    .append(Stream.chunk(Chunk.apply(4, 5)))
    .append(Stream.chunk(Chunk.apply(6, 7, 8)))
    .append(Stream.chunk(Chunk.empty[Int]))
    .append(Stream.chunk(Chunk.apply(9, 0)))
    .covary[F]

  def go[F[_], O](acc: Chunk[O], n: Int, s: Stream[F, O]): Pull[F, INothing, Option[(Chunk[O], Stream[F, O])]] =
    s.pull.uncons.flatMap {
      case None => Pull.output1(acc).as(Some(Stream.empty))
      case Some((hd, tl)) => ???
    }
    ???

  def stream1[F[_]] = stream[F].repeatPull(_.uncons.flatMap {
    case None =>
      println("None")
      Pull.pure(None)
    case Some((c, s)) =>
      println(c)
      Pull.pure(Some((c, s)))
  }.flatMap {
    case Some((c, s)) => Pull.output1(c).as(Some(s))
    case None => Pull.pure(None)
  })

  val run: IO[Unit] =
    for
      _ <- stream1[IO].compile.toList.flatMap(IO.println)
    yield ()
