package com.peknight.demo.js.stream

import cats.data.State
import cats.effect.Clock
import fs2.{Chunk, Pipe, Stream}

import scala.concurrent.duration.FiniteDuration

object StreamPipe:

  def state[F[_], S, O](s: S): Pipe[F, State[S, O], O] =
    in => in.scanChunks(s) { (ss, stateChunk) =>
      stateChunk.foldLeft((ss, Chunk.empty[O])) { case ((sss, chunk), state) =>
        val (ssss, o) = state.run(sss).value
        (ssss, chunk ++ Chunk(o))
      }
    }

  def takeEvery[F[_]: Clock, O](interval: FiniteDuration): Pipe[F, O, O] =
    in => in.zip(Stream.repeatEval(Clock[F].monotonic))
      .scanChunks[Option[FiniteDuration], (O, FiniteDuration), O](None) { (previousOption, originChunk) =>
        originChunk.foldLeft((previousOption, Chunk.empty[O])) { case ((prevOption, updatedChunk), (o, nano)) =>
          if prevOption.forall(prev => nano - prev >= interval) then (Some(nano), updatedChunk ++ Chunk(o))
          else (prevOption, updatedChunk)
        }
      }

