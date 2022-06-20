package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import fs2.text.{base64, hex, utf8}
import fs2.{Chunk, Pipe, Pull, Stream}

import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64
import javax.crypto.spec.SecretKeySpec
import javax.crypto.{Cipher, KeyGenerator, SecretKey}

object AesApp extends IOApp.Simple:

  val aesAlgo = "AES"
  def keySpec(key: Array[Byte]): IO[SecretKey] = IO(new SecretKeySpec(key, aesAlgo))
  val size = 16

  object ecb:
    object pkcs5padding:
//      val transformation = "AES/ECB/PKCS5Padding"
      val transformation = "AES/ECB/NoPadding"
      def encrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.chunkN(size).flatMap { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          val output = cipher.doFinal(chunk.toArray)
          println(s"e: input=${chunk.toArray.length}, output=${output.length}")
          Stream.chunk(Chunk.array(output))
        }
      def decrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.chunkN(size).flatMap { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          val output = cipher.doFinal(chunk.toArray)
          println(s"d: input=${chunk.toArray.length}, output=${output.length}")
          Stream.chunk(Chunk.array(output))
        }
    end pkcs5padding
  end ecb

  def javaEncrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
    val cipher: Cipher = Cipher.getInstance(ecb.pkcs5padding.transformation)
    val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    cipher.doFinal(input)

  def javaDecrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
    val cipher: Cipher = Cipher.getInstance(ecb.pkcs5padding.transformation)
    val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
    cipher.init(Cipher.DECRYPT_MODE, keySpec)
    cipher.doFinal(input)

  val run: IO[Unit] =
    for
      key <- keySpec("1234567890abcdef".getBytes(UTF_8))
      // 8ea36c8e64c178b321c41dd3c67cfeff8ea36c8e64c178b321c41dd3c67cfeff02dec5652c0215f93af69f009f0f6d58
      // 8ea36c8e64c178b321c41dd3c67cfeff8ea36c8e64c178b321c41dd3c67cfeff02dec5652c0215f93af69f009f0f6d58
      message = "Hello, world!abc" * 2

//      encryptedBytes = Stream(message).through(utf8.encode).through(ecb.pkcs5padding.encrypt(key))
//      decryptedBytes = encryptedBytes.through(ecb.pkcs5padding.decrypt(key))
//      encryptedHex = encryptedBytes.through(hex.encode).toList.mkString("")
//      decryptedMessage = decryptedBytes.through(utf8.decode).toList.mkString("")

      data = message.getBytes(UTF_8) ++ Array.fill(16)(16.toByte)
      javaEncryptedBytes = javaEncrypt(key.getEncoded, data)
      javaDecryptedBytes = javaDecrypt(key.getEncoded, javaEncryptedBytes)
      javaEncryptedHex = new BigInteger(1, javaEncryptedBytes).toString(16)
      javaDecryptedMessage = new String(javaDecryptedBytes, UTF_8)

      _ <- IO.println(s"Data length         : ${data.length}")
      _ <- IO.println(s"JavaEncrypted length: ${javaEncryptedBytes.length}")
      //      _ <- IO.println(s"EncryptedBase64     : $encryptedHex")
      _ <- IO.println(s"JavaEncryptedBase64 : $javaEncryptedHex")
      //      _ <- IO.println(s"Encrypted Correct   : ${Set(encryptedHex, javaEncryptedBase64).size == 1}")
      _ <- IO.println(s"Message             : $message")
      //      _ <- IO.println(s"DecryptedMessage    : $decryptedMessage")
      _ <- IO.println(s"JavaDecryptedMessage: $javaDecryptedMessage")
//      _ <- IO.println(s"Decrypted Correct   : ${Set(message, decryptedMessage, javaDecryptedMessage).size == 1}")
    yield ()
