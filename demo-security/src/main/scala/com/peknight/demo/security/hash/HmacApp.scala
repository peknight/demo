package com.peknight.demo.security.hash

import cats.effect.{IO, IOApp}
import fs2.text.{hex, utf8}
import fs2.{Chunk, Pipe, Pure, Stream}

import javax.crypto.spec.SecretKeySpec
import javax.crypto.{KeyGenerator, Mac, SecretKey}

object HmacApp extends IOApp.Simple:

  val hmacMd5Algo = "HMacMD5"
  val generateKey: IO[SecretKey] = IO(KeyGenerator.getInstance(hmacMd5Algo).generateKey())
  def keySpec(hKey: Array[Byte]): IO[SecretKey] = IO(new SecretKeySpec(hKey, hmacMd5Algo))
  def hmacMD5[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
    val mac: Mac = Mac.getInstance(hmacMd5Algo)
    mac.init(key)
    Stream.suspend {
      in.chunks.fold(mac) { (m, c) =>
        val bytes = c.toArraySlice
        m.update(bytes.values, bytes.offset, bytes.size)
        m
      }.flatMap(m => Stream.chunk(Chunk.array(m.doFinal())))
    }

  val run: IO[Unit] =
    for
      key1 <- generateKey
      // 打印随机生成的key:
      _ <- IO.println(Stream(key1.getEncoded*).through(hex.encode).toList)
      _ <- IO.println(Stream("HelloWorld").through(utf8.encode).through(hmacMD5(key1)).through(hex.encode).toList)
      _ <- IO.println("------")
      hKey: Array[Byte] = Array[Byte](106, 70, -110, 125, 39, -20, 52, 56, 85, 9, -19, -72, 52, -53, 52, -45, -6,
        119, -63, 30, 20, -83, -28, 77, 98, 109, -32, -76, 121, -106, 0, -74, -107, -114, -45, 104, -104, -8, 2, 121,
        6, 97, -18, -13, -63, -30, -125, -103, -80, -46, 113, -14, 68, 32, -46, 101, -116, -104, -81, -108, 122, 89,
        -106, -109)
      key2 <- keySpec(hKey)
      // [126, 59, 37, 63, 73, 90, 111, -96, -77, 15, 82, -74, 122, -55, -67, 54]
      _ <- IO.println(Stream("HelloWorld").through(utf8.encode).through(hmacMD5(key2)).toList)
    yield ()
