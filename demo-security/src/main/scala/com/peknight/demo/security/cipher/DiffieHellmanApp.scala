package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import com.peknight.demo.security.*

import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement

object DiffieHellmanApp extends IOApp.Simple:

  // 甲选择一个素数
  val p: BigInt = BigInt(509)
  // 底数`g` 任选
  val g: BigInt = BigInt(5)
  // 随机数a，隐藏 （视为甲的私钥）
  val a = 123
  // 甲计算：（视为甲的公钥）
  val A: BigInt = g pow a mod p

  // 甲 将 p g A 给 乙

  // 乙收到后
  // 生成随机数b，隐藏
  val b = 456
  // 乙计算：（视为乙的公钥）
  val B: BigInt = g pow b mod p
  // 乙再计算：
  val s1: BigInt = A pow b mod p

  // 乙 将 B 发给 甲

  // 甲计算：
  val s2: BigInt = B pow a mod p

  val diffieHellmanAlgo = "DH"

  // 生成本地KeyPair
  val generateKeyPair: IO[KeyPair] = IO {
    val kpGen: KeyPairGenerator = KeyPairGenerator.getInstance(diffieHellmanAlgo)
    kpGen.initialize(512)
    kpGen.generateKeyPair()
  }

  def generateSecretKey(privateKey: PrivateKey, receivedPubKeyBytes: Array[Byte]): IO[Array[Byte]] = IO {
    // 从byte[]恢复PublicKey
    val keySpec: X509EncodedKeySpec = X509EncodedKeySpec(receivedPubKeyBytes)
    val kf: KeyFactory = KeyFactory.getInstance(diffieHellmanAlgo)
    val receivedPublicKey: PublicKey = kf.generatePublic(keySpec)
    // 生成本地密钥
    val keyAgreement: KeyAgreement = KeyAgreement.getInstance(diffieHellmanAlgo)
    // 自己的privateKey
    keyAgreement.init(privateKey)
    // 对方的PublicKey
    keyAgreement.doPhase(receivedPublicKey, true)
    // 生成SecretKey密钥
    keyAgreement.generateSecret()
  }


  // 最终双方协商出的密钥s1/s2是121

  case class Person(name: String, publicKey: PublicKey, privateKey: PrivateKey, secretKey: Array[Byte]):
    def show: String =
      s"""
         |Name: $name
         |Private key: ${privateKey.getEncoded.hex}
         |Public key: ${publicKey.getEncoded.hex}
         |Secret key: ${secretKey.hex}
      """.trim.stripMargin

    val printKeys: IO[Unit] = IO.println(show)

  val bobAndAlice: IO[(Person, Person)] =
    for
      // 各自生成KeyPair
      bobKeyPair <- generateKeyPair
      bobPublic = bobKeyPair.getPublic
      bobPrivate = bobKeyPair.getPrivate

      aliceKeyPair <- generateKeyPair
      alicePublic = aliceKeyPair.getPublic
      alicePrivate = aliceKeyPair.getPrivate

      bobSecretKey <- generateSecretKey(bobPrivate, alicePublic.getEncoded)
      aliceSecretKey <- generateSecretKey(alicePrivate, bobPublic.getEncoded)
    yield (Person("Bob", bobPublic, bobPrivate, bobSecretKey), Person("Alice", alicePublic, alicePrivate, aliceSecretKey))

  val run: IO[Unit] =
    for
      _ <- IO.println(A)
      _ <- IO.println(B)
      _ <- IO.println(s1)
      _ <- IO.println(s2)
      _ <- IO.println("--------")
      bobAlice <- bobAndAlice
      bob = bobAlice._1
      alice = bobAlice._2
      _ <- bob.printKeys
      _ <- alice.printKeys
    yield ()
