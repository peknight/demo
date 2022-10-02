package com.peknight.demo.security.jwk

import com.chatwork.scala.jwk.RSAJWK

import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.security.{KeyPair, KeyPairGenerator, PrivateKey, PublicKey}

object RsaJwkApp extends App:
  val rsaAlgo: String = "RSA"
  val kpGen: KeyPairGenerator = KeyPairGenerator.getInstance(rsaAlgo)
  kpGen.initialize(2048)
  val kp: KeyPair = kpGen.generateKeyPair()
  val privateKey: RSAPrivateKey = kp.getPrivate.asInstanceOf[RSAPrivateKey]
  println(s"privateKey: $privateKey")
  val publicKey: RSAPublicKey = kp.getPublic.asInstanceOf[RSAPublicKey]
  println(s"publicKey: $publicKey")
  val rsaPrivateJwk = RSAJWK.fromKeyPair(publicKey, privateKey)
  println(s"rsaPrivateJwk: $rsaPrivateJwk")
  val rsaPublicJwk = RSAJWK.fromRSAPublicKey(publicKey)
  println(s"rsaPublicJwt: $rsaPublicJwk")
  rsaPrivateJwk.map(_.toRSAPrivateKey).foreach(k => println(s"privateKey: $k"))
  rsaPrivateJwk.map(_.toRSAPublicKey).foreach(k => println(s"publicKey: $k"))
  rsaPublicJwk.map { k =>
    k.toRSAPublicKey
  }.foreach(k => println(s"publicKey: $k"))
