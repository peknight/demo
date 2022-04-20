package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.{Pipe, Stream}

object StatefullyTransformingStreamsApp extends IOApp.Simple:

  def tk[F[_], O](n: Long): Pipe[F, O, O] =
    in => in.scanChunksOpt[Long, O, O](n) { n =>
      if n <= 0 then None
      else Some(c => c.size match {
        case m if m < n => (n - m, c)
        case _ => (0, c.take(n.toInt))
      })
    }

  val run = IO.println(Stream(1, 2, 3, 4).through(tk(2)).toList)
