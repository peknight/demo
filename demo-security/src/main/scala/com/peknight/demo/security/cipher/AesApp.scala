package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp, Sync}
import cats.syntax.functor.*
import com.peknight.demo.security.*
import fs2.text.{hex, utf8}
import fs2.{Chunk, Pipe, Pull, Stream}

import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import javax.crypto.{BadPaddingException, Cipher, SecretKey}

object AesApp extends IOApp.Simple:

  val aesAlgo = "AES"
  def keySpec(key: Array[Byte]): IO[SecretKey] = IO(new SecretKeySpec(key, aesAlgo))

  val ivParameterSpec: IO[IvParameterSpec] = IO {
    val sr: SecureRandom = SecureRandom.getInstanceStrong
    val iv: Array[Byte] = sr.generateSeed(16)
    new IvParameterSpec(iv)
  }

  object AES:
    // AES分组加密，每组16字节
    val blockSize = 16

    object ECB:
      // 由于原生不支持流操作，只能手动padding后使用NoPadding
      private[this] val noPaddingTransformation = "AES/ECB/NoPadding"

      object PKCS5Padding:

        private[this] val pkcs5PaddingTransformation = "AES/ECB/PKCS5Padding"

        // 加密
        def encrypt[F[_]: Sync](key: => SecretKey): Pipe[F, Byte, Byte] =
          _.chunkTimesN(blockSize).evalMapChunkLast {
            crypto(noPaddingTransformation, Cipher.ENCRYPT_MODE, key)
          }{
            crypto(pkcs5PaddingTransformation, Cipher.ENCRYPT_MODE, key)
          }

        // 解密
        def decrypt[F[_]: Sync](key: => SecretKey): Pipe[F, Byte, Byte] =
          _.chunkTimesN(blockSize).evalMapChunkLast {
            crypto(noPaddingTransformation, Cipher.DECRYPT_MODE, key)
          }{
            crypto(pkcs5PaddingTransformation, Cipher.DECRYPT_MODE, key)
          }

        private[this] def crypto[F[_]: Sync](transformation: String, opmode: Int, key: SecretKey)
        : Chunk[Byte] => F[Chunk[Byte]] =
          input => Sync[F].delay {
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(opmode, key)
            Chunk.array(cipher.doFinal(input.toArray))
          }

        // Java版加密
        def javaEncrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
          cipher.init(Cipher.ENCRYPT_MODE, keySpec)
          cipher.doFinal(input)

        // Java版解密
        def javaDecrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
          cipher.init(Cipher.DECRYPT_MODE, keySpec)
          cipher.doFinal(input)
      end PKCS5Padding

    end ECB

    object CBC:

      // 由于原生不支持流操作，只能手动padding后使用NoPadding
      private[this] val noPaddingTransformation = "AES/CBC/NoPadding"

      object PKCS5Padding:
        private[this] val pkcs5PaddingTransformation = "AES/CBC/PKCS5Padding"

        private[this] def crypto[F[_]: Sync](transformation: String, opmode: Int, key: SecretKey)
        : (IvParameterSpec, Chunk[Byte]) => F[Chunk[Byte]] =
          (ivps, input) => Sync[F].delay {
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(opmode, key, ivps)
            Chunk.array(cipher.doFinal(input.toArray))
          }

        private[this] def ivpsOutput(ivpsChunk: Chunk[Byte], outputChunk: Chunk[Byte]): (IvParameterSpec, Chunk[Byte]) =
          val nextIv: Array[Byte] = ivpsChunk.takeRight(blockSize).toArray
          (new IvParameterSpec(nextIv), outputChunk)

        private[this] def encrypt[F[_]: Sync](transformation: String, key: SecretKey)
        : (IvParameterSpec, Chunk[Byte]) => F[(IvParameterSpec, Chunk[Byte])] =
          (ivps, input) => crypto(transformation, Cipher.ENCRYPT_MODE, key).apply(ivps, input)
            .map(output => ivpsOutput(output, output))

        private[this] def decrypt[F[_]: Sync](transformation: String, key: SecretKey)
        : (IvParameterSpec, Chunk[Byte]) => F[(IvParameterSpec, Chunk[Byte])] =
          (ivps, input) => crypto(transformation, Cipher.DECRYPT_MODE, key).apply(ivps, input)
            .map(output => ivpsOutput(input, output))

        // 加密
        def encrypt[F[_]: Sync](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] =
          _.chunkTimesN(blockSize).evalScanChunksLast[F, Byte, Byte, IvParameterSpec](ivps) {
            encrypt(noPaddingTransformation, key)
          } {
            (ivps, c) => encrypt(pkcs5PaddingTransformation, key).apply(ivps, c).map(_._2)
          }

        // 解密
        def decrypt[F[_]: Sync](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] =
          _.chunkTimesN(blockSize).evalScanChunksLast[F, Byte, Byte, IvParameterSpec](ivps) {
            decrypt(noPaddingTransformation, key)
          } {
            (ivps, c) => decrypt(pkcs5PaddingTransformation, key).apply(ivps, c).map(_._2)
          }

        // Java版加密
        def javaEncrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
          javaCrypto(key, iv, input, Cipher.ENCRYPT_MODE)

        // Java版解密
        def javaDecrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
          javaCrypto(key, iv, input, Cipher.DECRYPT_MODE)

        private[this] def javaCrypto(key: Array[Byte], iv: Array[Byte], input: Array[Byte], opmode: Int): Array[Byte] =
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
          val ivps: IvParameterSpec = new IvParameterSpec(iv)
          cipher.init(opmode, keySpec, ivps)
          cipher.doFinal(input)
      end PKCS5Padding
    end CBC
  end AES

  val run: IO[Unit] =
    for
      key <- keySpec("1234567890abcdef1234567890abcdef".getBytes(UTF_8))
      ivps <- ivParameterSpec

      message = "Hello, world!" * 50

      ecbEncryptedBytes = Stream(message).through(utf8.encode).covary[IO].through(AES.ECB.PKCS5Padding.encrypt(key))
      ecbDecryptedBytes = ecbEncryptedBytes.through(AES.ECB.PKCS5Padding.decrypt(key))
      ecbEncryptedHex <- ecbEncryptedBytes.through(hex.encode).compile.toList.map(_.mkString(""))
      ecbDecryptedMessage <- ecbDecryptedBytes.through(utf8.decode).compile.toList.map(_.mkString(""))

      cbcEncryptedBytes = Stream(message).through(utf8.encode).covary[IO].through(AES.CBC.PKCS5Padding.encrypt(key, ivps))
      cbcDecryptedBytes = cbcEncryptedBytes.through(AES.CBC.PKCS5Padding.decrypt(key, ivps))
      cbcEncryptedHex <- cbcEncryptedBytes.through(hex.encode).compile.toList.map(_.mkString(""))
      cbcDecryptedMessage <- cbcDecryptedBytes.through(utf8.decode).compile.toList.map(_.mkString(""))

      data = message.getBytes(UTF_8)

      javaEcbEncryptedBytes = AES.ECB.PKCS5Padding.javaEncrypt(key.getEncoded, data)
      javaEcbDecryptedBytes = AES.ECB.PKCS5Padding.javaDecrypt(key.getEncoded, javaEcbEncryptedBytes)
      javaEcbEncryptedHex = Stream.chunk(Chunk.array(javaEcbEncryptedBytes)).through(hex.encode).toList.mkString("")
      javaEcbDecryptedMessage = new String(javaEcbDecryptedBytes, UTF_8)

      javaCbcEncryptedBytes = AES.CBC.PKCS5Padding.javaEncrypt(key.getEncoded, ivps.getIV, data)
      javaCbcDecryptedBytes = AES.CBC.PKCS5Padding.javaDecrypt(key.getEncoded, ivps.getIV, javaCbcEncryptedBytes)
      javaCbcEncryptedHex = Stream.chunk(Chunk.array(javaCbcEncryptedBytes)).through(hex.encode).toList.mkString("")
      javaCbcDecryptedMessage = new String(javaCbcDecryptedBytes, UTF_8)

      _ <- IO.println(s"Data length             : ${data.length}")
      _ <- IO.println(s"EcbEncryptedHex         : $ecbEncryptedHex")
      _ <- IO.println(s"JavaEcbEncryptedHex     : $javaEcbEncryptedHex")
      _ <- IO.println(s"CbcEncryptedHex         : $cbcEncryptedHex")
      _ <- IO.println(s"JavaCbcEncryptedHex     : $javaCbcEncryptedHex")
      _ <- IO.println(s"EcbEncrypted Correct    : ${Set(ecbEncryptedHex, javaEcbEncryptedHex).size == 1}")
      _ <- IO.println(s"CbcEncrypted Correct    : ${Set(cbcEncryptedHex, javaCbcEncryptedHex).size == 1}")
      _ <- IO.println(s"Message                 : $message")
      _ <- IO.println(s"EcbDecryptedMessage     : $ecbDecryptedMessage")
      _ <- IO.println(s"JavaEcbDecryptedMessage : $javaEcbDecryptedMessage")
      _ <- IO.println(s"CbcDecryptedMessage     : $cbcDecryptedMessage")
      _ <- IO.println(s"JavaCbcDecryptedMessage : $javaCbcDecryptedMessage")
      _ <- IO.println(s"EcbDecrypted Correct    : ${Set(message, ecbDecryptedMessage, javaEcbDecryptedMessage).size == 1}")
      _ <- IO.println(s"CbcDecrypted Correct    : ${Set(message, cbcDecryptedMessage, javaCbcDecryptedMessage).size == 1}")
    yield ()
