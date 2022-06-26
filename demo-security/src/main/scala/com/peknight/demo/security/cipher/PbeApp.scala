package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import org.bouncycastle.jce.provider.BouncyCastleProvider

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.{SecureRandom, Security}
import java.util.Base64
import javax.crypto.spec.{PBEKeySpec, PBEParameterSpec}
import javax.crypto.{Cipher, SecretKey, SecretKeyFactory}

object PbeApp extends IOApp.Simple:

  val pbeAlgo = "PBEwithSHA1and128bitAES-CBC-BC"

  def javaEncrypt(password: String, salt: Array[Byte], input: Array[Byte]): Array[Byte] =
    javaCrypto(password, salt, input, Cipher.ENCRYPT_MODE)

  def javaDecrypt(password: String, salt: Array[Byte], input: Array[Byte]): Array[Byte] =
    javaCrypto(password, salt, input, Cipher.DECRYPT_MODE)

  def javaCrypto(password: String, salt: Array[Byte], input: Array[Byte], opmode: Int): Array[Byte] =
    val keySpec: PBEKeySpec = new PBEKeySpec(password.toCharArray)
    val skeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(pbeAlgo)
    val skey: SecretKey = skeyFactory.generateSecret(keySpec)
    val pbeps: PBEParameterSpec = new PBEParameterSpec(salt, 1000)
    val cipher: Cipher = Cipher.getInstance(pbeAlgo)
    cipher.init(opmode, skey, pbeps)
    cipher.doFinal(input)

  val run: IO[Unit] =
    for
      _ <- IO(Security.addProvider(new BouncyCastleProvider))
      message = "Hello, world!"
      password = "hello12345"
      salt <- IO(SecureRandom.getInstanceStrong.generateSeed(16))
      _ <- IO(System.out.printf("salt: %032x\n", new BigInteger(1, salt)))
      data = message.getBytes(StandardCharsets.UTF_8)
      javaEncrypted = javaEncrypt(password, salt, data)
      _ <- IO.println(s"encrypted: ${Base64.getEncoder.encodeToString(javaEncrypted)} ${javaEncrypted.length}")
      javaDecrypted = javaDecrypt(password, salt, javaEncrypted)
      _ <- IO.println(s"decrypted: ${new String(javaDecrypted, StandardCharsets.UTF_8)}")
    yield ()

