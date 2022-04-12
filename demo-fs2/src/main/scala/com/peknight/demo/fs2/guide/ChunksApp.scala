package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.{Chunk, Stream}

/**
 * <a href="https://mpilquist.github.io/fs2-chunk-talk/">fs2.Chunk</a>
 *
 * A chunk is...
 * - a mostly immutable, mostly strict, finite sequence of values
 * - that supports mostly efficient index-based random access of elements
 * - that's memory efficient for all sizes
 * - that avoids unnecessary copying
 *
 * Why not use Vector?
 * - Streams move chunks
 * - If streams passed around vectors, we'd need to copy the underlying buffers.
 *
 * Not making a defensive copy for performance reasons：不会复制原数组，原数组内值变了会跟着变
 * Zero copy splitAt at cost of non-strictness and potentially increased memory usage
 *
 */
object ChunksApp extends IOApp.Simple {
  val s1c = Stream.chunk(Chunk.array(Array(1.0, 2.0, 3.0)))

  val run = for {
    _ <- IO.println(s1c.toList)
  } yield ()
}
