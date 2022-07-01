package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import com.peknight.demo.security.*

import java.nio.charset.StandardCharsets
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.*
import javax.crypto.Cipher

object RsaApp extends IOApp.Simple:

  val rsaAlgo = "RSA"

  case class Person(name: String, sk: PrivateKey, pk: PublicKey):
    def privateKey: Array[Byte] = sk.getEncoded
    def publicKey: Array[Byte] = pk.getEncoded
    def encrypt(message: Array[Byte]): IO[Array[Byte]] = IO {
      val cipher: Cipher = Cipher.getInstance(rsaAlgo)
      cipher.init(Cipher.ENCRYPT_MODE, pk)
      cipher.doFinal(message)
    }
    def decrypt(input: Array[Byte]): IO[Array[Byte]] = IO {
      val cipher: Cipher = Cipher.getInstance(rsaAlgo)
      cipher.init(Cipher.DECRYPT_MODE, sk)
      cipher.doFinal(input)
    }
  end Person

  def person(name: String): IO[Person] = IO {
    val kpGen: KeyPairGenerator = KeyPairGenerator.getInstance(rsaAlgo)
    kpGen.initialize(1024)
    val kp: KeyPair = kpGen.generateKeyPair()
    Person(name, kp.getPrivate, kp.getPublic)
  }

  val keyFactory: IO[KeyFactory] = IO(KeyFactory.getInstance(rsaAlgo))
  def publicKey(pkData: Array[Byte]): IO[PublicKey] =
    for
      kf <- keyFactory
    yield kf.generatePublic(X509EncodedKeySpec(pkData))

  def privateKey(skData: Array[Byte]): IO[PrivateKey] =
    for
      kf <- keyFactory
    yield kf.generatePrivate(PKCS8EncodedKeySpec(skData))

  val plain: Array[Byte] = "Hello, encrypt use RSA".getBytes(StandardCharsets.UTF_8)

  val run: IO[Unit] =
    for
      alice <- person("Alice")
      _ <- IO.println(s"public key: ${alice.pk.getEncoded.hex}")
      encrypted <- alice.encrypt(plain)
      _ <- IO.println(s"encrypted: ${encrypted.hex}")
      _ <- IO.println(s"private key: ${alice.sk.getEncoded.hex}")
      decrypted <- alice.decrypt(encrypted)
      _ <- IO.println(decrypted.utf8)
    yield ()
