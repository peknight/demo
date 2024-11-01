package com.peknight.demo.fs2.compression

import cats.effect.{IO, IOApp, Resource}
import fs2.compression.{Compression, DeflateParams, InflateParams, ZLibParams}
import fs2.{Chunk, Stream}
import scodec.bits.ByteVector

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.zip.{Deflater, DeflaterOutputStream, Inflater, InflaterInputStream}

object CompressionApp extends IOApp.Simple:
  val data: ByteVector =
    ByteVector.encodeUtf8("test test test test test test test test test test test test test test test test and stuff")
      .getOrElse(ByteVector.empty)

  val run: IO[Unit] =
    for
      _ <- IO.println(data)
      javaCompressed <- javaCompress(data)
      _ <- IO.println(javaCompressed)
      javaDecompressed <- javaDecompress(javaCompressed)
      _ <- IO.println(javaDecompressed)
      _ <- IO.println(javaDecompressed.decodeUtf8.getOrElse(""))
      fs2Compressed <- compress(data)
      _ <- IO.println(fs2Compressed)
      fs2Decompressed <- decompress(fs2Compressed)
      _ <- IO.println(fs2Decompressed)
      _ <- IO.println(fs2Decompressed.decodeUtf8.getOrElse(""))
    yield ()

  def compress(data: ByteVector): IO[ByteVector] =
    Stream.chunk(Chunk.byteVector(data))
      .through(Compression[IO].deflate(DeflateParams(level = DeflateParams.Level.EIGHT, header = ZLibParams.Header.GZIP)))
      .compile
      .toVector
      .map(ByteVector.apply)

  def decompress(compressedData: ByteVector): IO[ByteVector] =
    Stream.chunk(Chunk.byteVector(compressedData))
      .through(Compression[IO].inflate(InflateParams(header = ZLibParams.Header.GZIP)))
      .compile
      .toVector
      .map(ByteVector.apply)

  def javaCompress(data: ByteVector): IO[ByteVector] =
    val resource =
      for
        deflater <- Resource.make(IO(new Deflater(Deflater.DEFLATED, true)))(d => IO(d.end()))
        byteArrayOutputStream <- Resource.fromAutoCloseable(IO(new ByteArrayOutputStream()))
        deflaterOutputStream <- Resource.fromAutoCloseable(IO(new DeflaterOutputStream(byteArrayOutputStream, deflater)))
      yield
        (byteArrayOutputStream, deflaterOutputStream)
    resource.use { (byteArrayOutputStream, deflaterOutputStream) =>
      for
        _ <- IO(deflaterOutputStream.write(data.toArray))
        _ <- IO(deflaterOutputStream.finish())
        res <- IO(ByteVector(byteArrayOutputStream.toByteArray))
      yield
        res
    }

  def javaDecompress(compressedData: ByteVector): IO[ByteVector] =
    val resource =
      for
        inflater <- Resource.make(IO(new Inflater(true)))(i => IO(i.end()))
        byteArrayOutputStream <- Resource.fromAutoCloseable(IO(new ByteArrayOutputStream()))
        iis <- Resource.fromAutoCloseable(IO(new InflaterInputStream(new ByteArrayInputStream(compressedData.toArray), inflater)))
      yield
        (byteArrayOutputStream, iis)
    resource.use { (byteArrayOutputStream, iis) =>
      IO {
        var bytesRead = 0
        val buff = new Array[Byte](256)
        while ({bytesRead = iis.read(buff); bytesRead} != -1) {
          byteArrayOutputStream.write(buff, 0, bytesRead)
        }
        ByteVector(byteArrayOutputStream.toByteArray)
      }
    }
end CompressionApp
