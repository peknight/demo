package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import fs2.text.{hex, utf8}
import fs2.{Chunk, Pipe, Pull, Stream}

import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import javax.crypto.{Cipher, SecretKey}

object AesApp extends IOApp.Simple:

  val aesAlgo = "AES"
  def keySpec(key: Array[Byte]): IO[SecretKey] = IO(new SecretKeySpec(key, aesAlgo))

  val ivParameterSpec: IO[IvParameterSpec] = IO {
    val sr: SecureRandom = SecureRandom.getInstanceStrong
    val iv: Array[Byte] = sr.generateSeed(16)
    new IvParameterSpec(iv)
  }

  // 将流中的Chunk大小调整为n的整数倍，并直接输出Chunk。参考`fs2.Stream#chunkN`实现
  def chunkTimesN[F[_], O](n: Int): Pipe[F, O, Chunk[O]] =
    def go(acc: Chunk[O], s: Stream[F, O]): Pull[F, Chunk[O], Option[Stream[F, O]]] =
      s.pull.uncons.flatMap {
        // 流结束: 没有剩余Chunk => 结束，有剩余值 => 将余下的值作为一个Chunk输出后结束
        case None => if acc.isEmpty then Pull.pure(None) else Pull.output1(acc).as(None)
        // 流还有值
        case Some((hd, tl)) =>
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + hd.size
          // 小于n 打包留给流后续处理
          if size < n then go(acc ++ hd, tl)
          // 正好是整数倍，直接输出
          else if size % n == 0 then Pull.output1(acc ++ hd).as(Some(tl))
          else
            // 多余整数倍，把零头(sfx)提出来
            val (pfx, sfx) = hd.splitAt(hd.size - size % n)
            // 将整数倍的部分输出，零头拼到流的后续
            Pull.output1(acc ++ pfx).as(Some(tl.cons(sfx)))
      }
    in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream))
  end chunkTimesN

  object AES:
    // AES分组加密，每组16字节
    val blockSize = 16

    object ECB:
      // 由于原生不支持流操作，只能手动padding后使用NoPadding
      private[this] val transformation = "AES/ECB/NoPadding"

      // 加密
      def encrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        // 手动使用PKCS5Padding填充
        in.through(PKCS5Padding.padding).flatMap { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          Stream.chunk(Chunk.array(cipher.doFinal(chunk.toArray)))
        }

      // 解密
      def decrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        // 将密文分组（分成16字节的整数倍即可）
        in.through(chunkTimesN(blockSize)).map { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          Chunk.array(cipher.doFinal(chunk.toArray))
        // 手动使用PKCS5Padding把填充内容去除
        }.through(PKCS5Padding.unPadding)

      private[this] val javaTransformation = "AES/ECB/PKCS5Padding"

      // Java版加密
      def javaEncrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        cipher.doFinal(input)

      // Java版解密
      def javaDecrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        cipher.doFinal(input)
    end ECB

    object CBC:

      // 由于原生不支持流操作，只能手动padding后使用NoPadding
      private[this] val transformation = "AES/CBC/NoPadding"

      // 加密
      def encrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] = in =>
        // 手动使用PKCS5Padding填充
        in.through(PKCS5Padding.padding).scanChunks(ivps) { (ivps, chunks) =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key, ivps)
          val outputChunk = Chunk.array(cipher.doFinal(chunks.flatMap(identity).toArray))
          // 密文的最后一个分组用于下一个向量
          val nextIv: Array[Byte] = outputChunk.takeRight(blockSize).toArray
          (new IvParameterSpec(nextIv), outputChunk)
        }

      def decrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] = in =>
        // 将密文分组（分成16字节的整数倍即可）
        in.through(chunkTimesN(blockSize)).scanChunks(ivps) { (ivps, chunks) =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key, ivps)
          val inputChunk = chunks.flatMap(identity)
          val outputChunk = Chunk.array(cipher.doFinal(inputChunk.toArray))
          // 密文的最后一个分组用于下一个向量
          val nextIv: Array[Byte] = inputChunk.takeRight(blockSize).toArray
          (new IvParameterSpec(nextIv), Chunk(outputChunk))
        // 手动使用PKCS5Padding把填充内容去除
        }.through(PKCS5Padding.unPadding)

      private[this] val javaTransformation = "AES/CBC/PKCS5Padding"

      // Java版加密
      def javaEncrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
        javaCipher(key, iv, input, Cipher.ENCRYPT_MODE)

      // Java版解密
      def javaDecrypt(key: Array[Byte], iv: Array[Byte], input: Array[Byte]): Array[Byte] =
        javaCipher(key, iv, input, Cipher.DECRYPT_MODE)

      private[this] def javaCipher(key: Array[Byte], iv: Array[Byte], input: Array[Byte], opmode: Int): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        val ivps: IvParameterSpec = new IvParameterSpec(iv)
        cipher.init(opmode, keySpec, ivps)
        cipher.doFinal(input)
    end CBC
  end AES

  object PKCS5Padding:
    // 填充分组
    def padding[F[_]]: Pipe[F, Byte, Chunk[Byte]] =
      def go(acc: Chunk[Byte], s: Stream[F, Byte]): Pull[F, Chunk[Byte], Option[Stream[F, Byte]]] =
        s.pull.uncons.flatMap {
          // 流结束，按PKCS5Padding填充
          case None =>
            val paddingSize = AES.blockSize - acc.size
            Pull.output1(acc ++ Chunk.seq(List.fill(paddingSize)(paddingSize.toByte))).as(None)
          // 流还有值
          case Some((hd, tl)) =>
            // 判断此前剩余Chunk + 当前Chunk大小
            val size = acc.size + hd.size
            // 小于分组大小 打包留给流后续处理
            if size < AES.blockSize then go(acc ++ hd, tl)
            // 正好是整数倍，直接输出
            else if size % AES.blockSize == 0 then Pull.output1(acc ++ hd).as(Some(tl))
            else
              // 多余整数倍，把零头(sfx)提出来
              val (pfx, sfx) = hd.splitAt(hd.size - size % AES.blockSize)
              // 将整数倍的部分输出，零头拼到流的后续
              Pull.output1(acc ++ pfx).as(Some(tl.cons(sfx)))
        }
      in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream))
    end padding

    // 去除填充
    def unPadding[F[_]]: Pipe[F, Chunk[Byte], Byte] =
      def go(acc: Chunk[Byte], s: Stream[F, Chunk[Byte]]): Pull[F, Byte, Option[Stream[F, Chunk[Byte]]]] =
        s.pull.uncons.flatMap {
          case None =>
            // 流结束 校验填充内容并去除
            val padSize: Byte = acc.last.getOrElse(0)
            assert((acc.size - padSize).until(acc.size).forall(acc(_) == padSize))
            Pull.output(acc.dropRight(padSize)).as(None)
          case Some((hd, tl)) =>
            // 流还有值，留下一个分组用于最后处理去除填充，其余分组直接输出
            val (pfx, sfx) = hd.splitAt(hd.size - 1)
            Pull.output(acc ++ pfx.flatMap(identity)) >> go(sfx.flatMap(identity), tl)
        }
      in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream))
    end unPadding
  end PKCS5Padding

  val run: IO[Unit] =
    for
      key <- keySpec("1234567890abcdef1234567890abcdef".getBytes(UTF_8))
      ivps <- ivParameterSpec

      message = "Hello, world!"

      ecbEncryptedBytes = Stream(message).through(utf8.encode).through(AES.ECB.encrypt(key))
      ecbDecryptedBytes = ecbEncryptedBytes.through(AES.ECB.decrypt(key))
      ecbEncryptedHex = ecbEncryptedBytes.through(hex.encode).toList.mkString("")
      ecbDecryptedMessage = ecbDecryptedBytes.through(utf8.decode).toList.mkString("")

      cbcEncryptedBytes = Stream(message).through(utf8.encode).through(AES.CBC.encrypt(key, ivps))
      cbcDecryptedBytes = cbcEncryptedBytes.through(AES.CBC.decrypt(key, ivps))
      cbcEncryptedHex = cbcEncryptedBytes.through(hex.encode).toList.mkString("")
      cbcDecryptedMessage = cbcDecryptedBytes.through(utf8.decode).toList.mkString("")

      data = message.getBytes(UTF_8)

      javaEcbEncryptedBytes = AES.ECB.javaEncrypt(key.getEncoded, data)
      javaEcbDecryptedBytes = AES.ECB.javaDecrypt(key.getEncoded, javaEcbEncryptedBytes)
      javaEcbEncryptedHex = new BigInteger(1, javaEcbEncryptedBytes).toString(16)
      javaEcbDecryptedMessage = new String(javaEcbDecryptedBytes, UTF_8)

      javaCbcEncryptedBytes = AES.CBC.javaEncrypt(key.getEncoded, ivps.getIV, data)
      javaCbcDecryptedBytes = AES.CBC.javaDecrypt(key.getEncoded, ivps.getIV, javaCbcEncryptedBytes)
      javaCbcEncryptedHex = new BigInteger(1, javaCbcEncryptedBytes).toString(16)
      javaCbcDecryptedMessage = new String(javaCbcDecryptedBytes, UTF_8)

      _ <- IO.println(s"Data length             : ${data.length}")
      _ <- IO.println(s"EcbEncryptedHex         : $ecbEncryptedHex")
      _ <- IO.println(s"JavaEcbEncryptedHex     : $javaEcbEncryptedHex")
      _ <- IO.println(s"CbcEncryptedHex         : $cbcEncryptedHex")
      _ <- IO.println(s"JavaCbcEncryptedHex     : $javaCbcEncryptedHex")
      _ <- IO.println(s"EcbEncrypted Correct    : ${Set(ecbEncryptedHex, javaEcbEncryptedHex).size == 1}")
      _ <- IO.println(s"CbcEncrypted Correct    : ${Set(cbcEncryptedHex, javaCbcEncryptedHex).size == 1}")
      _ <- IO.println(s"Message                 : $message")
      _ <- IO.println(s"EcbDecryptedMessage     : $ecbDecryptedMessage")
      _ <- IO.println(s"JavaEcbDecryptedMessage : $javaEcbDecryptedMessage")
      _ <- IO.println(s"CbcDecryptedMessage     : $cbcDecryptedMessage")
      _ <- IO.println(s"JavaCbcDecryptedMessage : $javaCbcDecryptedMessage")
      _ <- IO.println(s"EcbDecrypted Correct    : ${Set(message, ecbDecryptedMessage, javaEcbDecryptedMessage).size == 1}")
      _ <- IO.println(s"CbcDecrypted Correct    : ${Set(message, cbcDecryptedMessage, javaCbcDecryptedMessage).size == 1}")
    yield ()
