package com.peknight.demo.js.stream

import cats.data.State
import fs2.{Chunk, Stream}

object StreamOps:

  extension [F[_], S, O](stream: Stream[F, State[S, O]])
    def scanState(s: S): Stream[F, O] =
      stream.scanChunks(s) { (ss, stateChunk) =>
        stateChunk.foldLeft((ss, Chunk.empty[O])) { case ((sss, chunk), state) =>
          val (ssss, o) = state.run(sss).value
          (ssss, chunk ++ Chunk(o))
        }
      }
  end extension



