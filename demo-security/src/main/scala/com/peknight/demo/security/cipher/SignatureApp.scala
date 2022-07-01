package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import com.peknight.demo.security.*

import java.nio.charset.StandardCharsets
import java.security.*

object SignatureApp extends IOApp.Simple:

  val rsaAlgo = "RSA"

  val sha1WithRsaAlgo = "SHA1withRSA"

  // 生成rsa公钥/私钥
  val keyPair: IO[KeyPair] = IO {
    val kpGen: KeyPairGenerator = KeyPairGenerator.getInstance(rsaAlgo)
    kpGen.initialize(1024)
    kpGen.generateKeyPair()
  }

  // 待签名消息
  val message: Array[Byte] = "Hello, I am Bob!".getBytes(StandardCharsets.UTF_8)

  // 用私钥签名
  def sign(sk: PrivateKey, message: Array[Byte]): IO[Array[Byte]] = IO {
    val s: Signature = Signature.getInstance(sha1WithRsaAlgo)
    s.initSign(sk)
    s.update(message)
    s.sign()
  }

  // 用公钥验证
  def verify(pk: PublicKey, message: Array[Byte], signed: Array[Byte]): IO[Boolean] = IO {
    val v: Signature = Signature.getInstance(sha1WithRsaAlgo)
    v.initVerify(pk)
    v.update(message)
    v.verify(signed)
  }

  val run: IO[Unit] =
    for
      kp <- keyPair
      sk = kp.getPrivate()
      pk = kp.getPublic()
      signed <- sign(sk, message)
      _ <- IO.println(s"signature: ${signed.hex}")
      valid <- verify(pk, message, signed)
      _ <- IO.println(s"valid? $valid")
    yield ()
