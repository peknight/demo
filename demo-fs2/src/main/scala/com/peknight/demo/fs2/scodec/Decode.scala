package com.peknight.demo.fs2.scodec

import cats.effect.{IO, IOApp}
import fs2.Stream
import fs2.interop.scodec.StreamDecoder
import fs2.io.file.{Files, Path}
import scodec.bits.ByteVector
import scodec.codecs.{bytes, int32}

object Decode extends IOApp.Simple:
  val frames: StreamDecoder[ByteVector] = StreamDecoder.many(int32).flatMap {numBytes => StreamDecoder.once(bytes(numBytes))}
  val filePath = Path("largefile.bin")
  val s: Stream[IO, ByteVector] = Files[IO].readAll(filePath).through(frames.toPipeByte)

  val run = s.compile.count.flatMap(cnt => IO.println(s"Read $cnt frames."))
