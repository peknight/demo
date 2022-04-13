package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.{Pipe, Pull, Stream}

object TransformingStreamsUsingPullsApp extends IOApp.Simple{

  val p1 = Pull.output1(1)

  // equivalent to `Stream(1)`
  val s1 = p1.stream

  def tk[F[_], O](n: Long): Pipe[F, O, O] = {
    def go(s: Stream[F, O], n: Long): Pull[F, O, Unit] = {
      s.pull.uncons.flatMap {
        case Some((hd, tl)) => hd.size match {
          case m if m <= n => Pull.output(hd) >> go(tl, n - m)
          case _ => Pull.output(hd.take(n.toInt))
        }
        case None => Pull.done
      }
    }
    in => go(in, n).stream
  }

  def tkViaTake[F[_], O](n: Long): Pipe[F, O, O] = in => in.pull.take(n).void.stream

  val run = for {
    _ <- IO.println(s1.toList)
    /*
     * Pulls form a monad in their result `R` and can be composed using monadic operations.
     * The following code produces a `Pull` corresponding to `Stream(1, 2)`.
     */
    _ <- IO.println((p1 >> Pull.output1(2)).stream.toList)
    /*
     * A `Pull` can be created from a stream using a variety of operations accessed under the `pull` function.
     * For example `echo` converts a stream to its corresponding pull representation.
     */
    _ <- IO.println(s1.pull.echo)
    /*
     * A more useful pull is created by `uncons`. This constructs a pull that pulls the next chunk from the stream
     */
    _ <- IO.println(s1.pull.uncons)
    _ <- IO.println(Stream(1, 2, 3, 4).through(tk(2)).toList)
    _ <- IO.println(Stream(1, 2, 3, 4).through(tkViaTake(2)).toList)

    _ <- IO.println(Stream("ok", "skip", "next ok", "skip", "told you")
      .pull
      // 失败时返回剩余的流数据
      .takeWhile(o => o != "skip")
      .flatMap {
        case None => Pull.pure(None)
        // 去掉第一个失败的"skip"继续
        case Some(s) => s.drop(1).pull.echo
      }
      .void
      .stream.toList
    )
  } yield ()
}
