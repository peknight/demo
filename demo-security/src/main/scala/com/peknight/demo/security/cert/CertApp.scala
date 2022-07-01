package com.peknight.demo.security.cert

import cats.effect.{IO, IOApp, Resource}
import com.peknight.demo.security.*

import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.security.{Key, KeyStore, PrivateKey, Signature}
import java.security.cert.{Certificate, X509Certificate}
import javax.crypto.Cipher
import scala.io.Source

object CertApp extends IOApp.Simple:

  val message: Array[Byte] = "Hello, use X.509 cert!".getBytes(StandardCharsets.UTF_8)

  def loadKeyStore(keyStoreFile: String, password: String): IO[KeyStore] =
    Resource.fromAutoCloseable(IO.blocking(getClass.getResourceAsStream(keyStoreFile))).use { inputStream => IO.blocking {
      val ks: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)
      ks.load(inputStream, password.toCharArray)
      ks
    }}

  def encrypt(certificate: Certificate, message: Array[Byte]): IO[Array[Byte]] = IO {
    val cipher: Cipher = Cipher.getInstance(certificate.getPublicKey.getAlgorithm)
    cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey)
    cipher.doFinal(message)
  }

  def decrypt(privateKey: Key, data: Array[Byte]): IO[Array[Byte]] = IO {
    val cipher: Cipher = Cipher.getInstance(privateKey.getAlgorithm)
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    cipher.doFinal(data)
  }

  def sign(privateKey: PrivateKey, certificate: X509Certificate, message: Array[Byte]): IO[Array[Byte]] = IO {
    val signature: Signature = Signature.getInstance(certificate.getSigAlgName)
    signature.initSign(privateKey)
    signature.update(message)
    signature.sign()
  }

  def verify(certificate: X509Certificate, message: Array[Byte], sig: Array[Byte]): IO[Boolean] = IO {
    val signature: Signature = Signature.getInstance(certificate.getSigAlgName)
    signature.initVerify(certificate)
    signature.update(message)
    signature.verify(sig)
  }

  val run: IO[Unit] =
    for
      ks <- loadKeyStore("/my.keystore", "123456")
      privateKey = ks.getKey("mycert", "123456".toCharArray)
      certificate = ks.getCertificate("mycert")
      encrypted <- encrypt(certificate, message)
      _ <- IO.println(s"encrypted: ${encrypted.hex}")
      decrypted <- decrypt(privateKey, encrypted)
      _ <- IO.println(s"decrypted: ${decrypted.utf8}")
      _ <- (privateKey, certificate) match
        case (sk: PrivateKey, cert: X509Certificate) =>
          for
            signed <- sign(sk, cert, message)
            _ <- IO.println(s"signature: ${signed.hex}")
            verified <- verify(cert, message, signed)
            _ <- IO.println(s"verify: $verified")
          yield ()
        case _ => IO.println("PrivateKey/X509Certificate not match")
    yield ()
