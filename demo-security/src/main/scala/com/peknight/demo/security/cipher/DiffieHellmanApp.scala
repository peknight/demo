package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}

import java.security.{PrivateKey, PublicKey}

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

  // 最终双方协商出的密钥s1/s2是121

  case class Person(name: String, publicKey: PublicKey, privateKey: PrivateKey, secretKey: Array[Byte])

  val run: IO[Unit] =
    for
      _ <- IO.println(A)
      _ <- IO.println(B)
      _ <- IO.println(s1)
      _ <- IO.println(s2)
    yield ()
