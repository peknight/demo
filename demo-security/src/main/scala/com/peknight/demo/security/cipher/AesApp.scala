package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}

import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64
import javax.crypto.{Cipher, KeyGenerator, SecretKey}
import javax.crypto.spec.SecretKeySpec

object AesApp extends IOApp.Simple:

  val generateKey: IO[SecretKey] = IO(KeyGenerator.getInstance("AES").generateKey())

  val message = "Hello, world!"
  val key: Array[Byte] = "1234567890abcdef".getBytes(UTF_8)
  val data: Array[Byte] = message.getBytes(UTF_8)

  val encrypted: Array[Byte] = ecbEncrypt(key, data)
  println(s"Encrypted: ${Base64.getEncoder.encodeToString(encrypted)}")
  val decrypted: Array[Byte] = ecbDecrypt(key, encrypted)
  println(s"Decrypted: ${new String(decrypted, UTF_8)}")

  def ecbEncrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val keySpec: SecretKey = new SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    cipher.doFinal(input)

  def ecbDecrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val keySpec: SecretKey = new SecretKeySpec(key, "AES")
    cipher.init(Cipher.DECRYPT_MODE, keySpec)
    cipher.doFinal(input)

  val run: IO[Unit] =
    for
      key <- generateKey
      _ <- IO.println(key.getEncoded.length)
      e <- IO(ecbEncrypt(key.getEncoded, data))
      _ <- IO.println(e.mkString(", "))
      d <- IO(ecbDecrypt(key.getEncoded, e))
      _ <- IO.println(new String(d, UTF_8))
    yield ()
