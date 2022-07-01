package com.peknight.demo

import fs2.{Chunk, Stream, text}

package object security:
  extension (bytes: Array[Byte])
    def hex: String = Stream.chunk(Chunk.array(bytes)).through(text.hex.encode).toList.mkString("")
    def utf8: String = Stream.chunk(Chunk.array(bytes)).through(text.utf8.decode).toList.mkString("")
