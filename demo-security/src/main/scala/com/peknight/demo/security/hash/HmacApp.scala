package com.peknight.demo.security.hash

import cats.effect.{IO, IOApp}

import java.math.BigInteger
import java.util
import javax.crypto.{KeyGenerator, Mac, SecretKey}
import javax.crypto.spec.SecretKeySpec

object HmacApp extends IOApp.Simple:

  {
    val keyGen: KeyGenerator = KeyGenerator.getInstance("HmacMD5")
    val key: SecretKey = keyGen.generateKey
    // 打印随机生成的key:
    val skey: Array[Byte] = key.getEncoded
    System.out.println(new BigInteger(1, skey).toString(16))
    val mac: Mac = Mac.getInstance("HmacMD5")
    mac.init(key)
    mac.update("HelloWorld".getBytes("UTF-8"))
    val result: Array[Byte] = mac.doFinal
    System.out.println(new BigInteger(1, result).toString(16))
  }

  {
    val hkey: Array[Byte] = Array[Byte](106, 70, -(110), 125, 39, -(20), 52, 56, 85, 9, -(19), -(72), 52, -(53), 52, -(45), -(6), 119, -(63), 30, 20, -(83), -(28), 77, 98, 109, -(32), -(76), 121, -(106), 0, -(74), -(107), -(114), -(45), 104, -(104), -(8), 2, 121, 6, 97, -(18), -(13), -(63), -(30), -(125), -(103), -(80), -(46), 113, -(14), 68, 32, -(46), 101, -(116), -(104), -(81), -(108), 122, 89, -(106), -(109))

    val key: SecretKey = new SecretKeySpec(hkey, "HmacMD5")
    val mac: Mac = Mac.getInstance("HmacMD5")
    mac.init(key)
    mac.update("HelloWorld".getBytes("UTF-8"))
    val result: Array[Byte] = mac.doFinal
    System.out.println(util.Arrays.toString(result))
    // [126, 59, 37, 63, 73, 90, 111, -96, -77, 15, 82, -74, 122, -55, -67, 54]
  }

  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield ()
