package com.peknight.demo.playground.fs2.pull

import cats.effect.{IO, IOApp}
import fs2.{Chunk, INothing, Pipe, Pull, Stream}

object ChunksApp extends IOApp.Simple:

  def stream[F[_]]: Stream[F, Int] = Stream.chunk(Chunk.apply(1, 2, 3))
    .append(Stream.chunk(Chunk.apply(4, 5, 6, 7)))
    .append(Stream.chunk(Chunk.apply(8, 9, 10, 11, 12)))
    .append(Stream.chunk(Chunk.empty[Int]))
    .append(Stream.chunk(Chunk.apply(13, 14)))
    .covary[F]

  def chunkTimesN[F[_], O](n: Int): Pipe[F, O, Chunk[O]] =
    def go(acc: Chunk[O], s: Stream[F, O]): Pull[F, INothing, Option[(Chunk[O], Stream[F, O])]] =
      s.pull.uncons.flatMap {
        case None => if acc.isEmpty then Pull.pure(None) else Pull.pure(Some((acc, Stream.empty)))
        case Some((hd, tl)) =>
          val size = acc.size + hd.size
          if size < n then go(acc ++ hd, tl)
          else if size % n == 0 then Pull.pure(Some((acc ++ hd) -> tl))
          else
            val (pfx, sfx) = hd.splitAt(hd.size - size % n)
            Pull.pure(Some((acc ++ pfx) -> tl.cons(sfx)))
      }
    in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream).flatMap {
      case Some((c, s)) => Pull.output1(c).as(Some(s))
      case None => Pull.pure(None)
    })

  val run: IO[Unit] =
    for
      _ <- stream[IO].through(chunkTimesN(1)).compile.toList.flatMap(IO.println)
      _ <- stream[IO].through(chunkTimesN(2)).compile.toList.flatMap(IO.println)
      _ <- stream[IO].through(chunkTimesN(3)).compile.toList.flatMap(IO.println)
      _ <- stream[IO].through(chunkTimesN(4)).compile.toList.flatMap(IO.println)
      _ <- stream[IO].through(chunkTimesN(5)).compile.toList.flatMap(IO.println)
    yield ()
