package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
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
      private[this] val pkcs5PaddingTransformation = "AES/ECB/PKCS5Padding"

      // 加密
      def encrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] =
        mapChunkTimesNLast[F, Byte, Byte](blockSize){ chunk =>
          val cipher: Cipher = Cipher.getInstance(noPaddingTransformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          Chunk.array(cipher.doFinal(chunk.toArray))
        }{ chunk =>
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          Chunk.array(cipher.doFinal(chunk.toArray))
        }

      // 解密
      def decrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] =
        mapChunkTimesNLast[F, Byte, Byte](blockSize){ chunk =>
          val cipher: Cipher = Cipher.getInstance(noPaddingTransformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          Chunk.array(cipher.doFinal(chunk.toArray))
        }{ chunk =>
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          Chunk.array(cipher.doFinal(chunk.toArray))
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
    end ECB

    object CBC:

      // 由于原生不支持流操作，只能手动padding后使用NoPadding
      private[this] val noPaddingTransformation = "AES/CBC/NoPadding"
      private[this] val pkcs5PaddingTransformation = "AES/CBC/PKCS5Padding"

      // 加密
      def encrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] =
        scanChunkTimesNLast[F, Byte, Byte, IvParameterSpec](blockSize)(ivps) { (ivps, chunk) =>
          val cipher: Cipher = Cipher.getInstance(noPaddingTransformation)
          cipher.init(Cipher.ENCRYPT_MODE, key, ivps)
          val outputChunk = Chunk.array(cipher.doFinal(chunk.toArray))
          // 密文的最后一个分组用于下一个向量
          val nextIv: Array[Byte] = outputChunk.takeRight(blockSize).toArray
          (new IvParameterSpec(nextIv), outputChunk)
        } { (ivps, chunk) =>
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          cipher.init(Cipher.ENCRYPT_MODE, key, ivps)
          Chunk.array(cipher.doFinal(chunk.toArray))
        }

      def decrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] =
        scanChunkTimesNLast[F, Byte, Byte, IvParameterSpec](blockSize)(ivps) { (ivps, chunk) =>
          val cipher: Cipher = Cipher.getInstance(noPaddingTransformation)
          cipher.init(Cipher.DECRYPT_MODE, key, ivps)
          val outputChunk = Chunk.array(cipher.doFinal(chunk.toArray))
          // 密文的最后一个分组用于下一个向量
          val nextIv: Array[Byte] = chunk.takeRight(blockSize).toArray
          (new IvParameterSpec(nextIv), outputChunk)
        } { (ivps, chunk) =>
          val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
          cipher.init(Cipher.DECRYPT_MODE, key, ivps)
          Chunk.array(cipher.doFinal(chunk.toArray))
        }

      // Java版加密
      def javaEncrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
        javaCipher(key, iv, input, Cipher.ENCRYPT_MODE)

      // Java版解密
      def javaDecrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
        javaCipher(key, iv, input, Cipher.DECRYPT_MODE)

      private[this] def javaCipher(key: Array[Byte], iv: Array[Byte], input: Array[Byte], opmode: Int): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(pkcs5PaddingTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        val ivps: IvParameterSpec = new IvParameterSpec(iv)
        cipher.init(opmode, keySpec, ivps)
        cipher.doFinal(input)
    end CBC
  end AES

  val run: IO[Unit] =
    for
      key <- keySpec("1234567890abcdef1234567890abcdef".getBytes(UTF_8))
      ivps <- ivParameterSpec

      message = "Hello, world!" * 50

      ecbEncryptedBytes = Stream(message).through(utf8.encode).through(AES.ECB.encrypt(key))
      ecbDecryptedBytes = ecbEncryptedBytes.through(AES.ECB.decrypt(key))
      ecbEncryptedHex = ecbEncryptedBytes.through(hex.encode).toList.mkString("")
      ecbDecryptedMessage = ecbDecryptedBytes.through(utf8.decode).toList.mkString("")

      cbcEncryptedBytes = Stream(message).through(utf8.encode).through(AES.CBC.encrypt(key, ivps))
      cbcDecryptedBytes = cbcEncryptedBytes.through(AES.CBC.decrypt(key, ivps))
      cbcEncryptedHex = cbcEncryptedBytes.through(hex.encode).toList.mkString("")
      cbcDecryptedMessage = cbcDecryptedBytes.through(utf8.decode).toList.mkString("")

      data = message.getBytes(UTF_8)

      javaEcbEncryptedBytes = AES.ECB.javaEncrypt(key.getEncoded, data)
      javaEcbDecryptedBytes = AES.ECB.javaDecrypt(key.getEncoded, javaEcbEncryptedBytes)
      javaEcbEncryptedHex = Stream.chunk(Chunk.array(javaEcbEncryptedBytes)).through(hex.encode).toList.mkString("")
      javaEcbDecryptedMessage = new String(javaEcbDecryptedBytes, UTF_8)

      javaCbcEncryptedBytes = AES.CBC.javaEncrypt(key.getEncoded, ivps.getIV, data)
      javaCbcDecryptedBytes = AES.CBC.javaDecrypt(key.getEncoded, ivps.getIV, javaCbcEncryptedBytes)
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
