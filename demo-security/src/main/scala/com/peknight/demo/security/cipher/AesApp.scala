package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import fs2.text.{base64, hex, utf8}
import fs2.{Chunk, INothing, Pipe, Pull, Stream}
import scodec.bits.ByteVector

import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import javax.crypto.{Cipher, KeyGenerator, SecretKey}

object AesApp extends IOApp.Simple:

  val aesAlgo = "AES"
  def keySpec(key: Array[Byte]): IO[SecretKey] = IO(new SecretKeySpec(key, aesAlgo))

  val ivParameterSpec: IO[IvParameterSpec] = IO {
    val sr: SecureRandom = SecureRandom.getInstanceStrong
    val iv: Array[Byte] = sr.generateSeed(16)
    new IvParameterSpec(iv)
  }

  def chunkTimesN[F[_]](n: Int): Pipe[F, Byte, ByteVector] =
    def go(acc: ByteVector, s: Stream[F, Byte]): Pull[F, INothing, Option[(ByteVector, Stream[F, Byte])]] =
      s.pull.uncons.flatMap {
        case None =>
          if acc.isEmpty then Pull.pure(None)
          else Pull.pure(Some((acc, Stream.empty)))
        case Some((hd, tl)) =>
          val size = acc.size + hd.size
          if size < n then go(acc ++ hd.toByteVector, tl)
          else if size % n == 0 then Pull.pure(Some((acc ++ hd.toByteVector) -> tl))
          else
            val (pfx, sfx) = hd.splitAt((hd.size - size % n).toInt)
            Pull.pure(Some((acc ++ pfx.toByteVector) -> tl.cons(sfx)))
      }
    in => in.repeatPull(pull => go(ByteVector.empty, pull.echo.stream).flatMap {
      case Some((c, s)) => Pull.output1(c).as(Some(s))
      case None => Pull.pure(None)
    })
  end chunkTimesN

  object AES:
    val blockSize = 16
    object ECB:
      private[this] val transformation = "AES/ECB/NoPadding"

      def encrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.through(PKCS5Padding.padding).flatMap { bv =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          val output = cipher.doFinal(bv.toArray)
          Stream.chunk(Chunk.array(output))
        }

      def decrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.through(chunkTimesN(blockSize)).map { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          val output = cipher.doFinal(chunk.toArray)
          ByteVector(output)
        }.through(PKCS5Padding.unPadding)

      private[this] val javaTransformation = "AES/ECB/PKCS5Padding"

      def javaEncrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        cipher.doFinal(input)

      def javaDecrypt(key: Array[Byte], input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        cipher.doFinal(input)
    end ECB

    object CBC:
      private[this] val transformation = "AES/CBC/NoPadding"

      private[this] val javaTransformation = "AES/CBC/PKCS5Padding"

      def encrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] = in =>
        in.through(PKCS5Padding.padding).scanChunks(ivps) { (ivps, chunks) =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key, ivps)
          val output = cipher.doFinal(ByteVector.concat(chunks.iterator).toArray)
          val outputVector = ByteVector(output)
          val nextIv: Array[Byte] = outputVector.splitAt(outputVector.size - blockSize)._2.toArray
          (new IvParameterSpec(nextIv), Chunk.array(output))
        }

      def decrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] = in =>
        in.through(chunkTimesN(blockSize)).scanChunks(ivps) { (ivps, chunks) =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key, ivps)
          val output = cipher.doFinal(ByteVector.concat(chunks.iterator).toArray)
          val outputVector = ByteVector(output)
          val nextIv: Array[Byte] = outputVector.splitAt(outputVector.size - blockSize)._2.toArray
          (new IvParameterSpec(nextIv), Chunk(outputVector))
        }.through(PKCS5Padding.unPadding)

      def javaEncrypt(key: Array[Byte], ivps: IvParameterSpec, input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps)
        cipher.doFinal(input)

      def javaDecrypt(key: Array[Byte], ivps: IvParameterSpec, input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivps)
        cipher.doFinal(input)

    end CBC
  end AES

  object PKCS5Padding:
    def padding[F[_]]: Pipe[F, Byte, ByteVector] =
      def go(acc: ByteVector, s: Stream[F, Byte]): Pull[F, INothing, Option[(ByteVector, Boolean, Stream[F, Byte])]] =
        s.pull.uncons.flatMap {
          case None =>
            val paddingSize = AES.blockSize - acc.size
            Pull.pure(Some((acc ++ ByteVector.fill(paddingSize)(paddingSize.toByte), true, Stream.empty)))
          case Some((hd, tl)) =>
            val size = acc.size + hd.size
            if size < AES.blockSize then go(acc ++ hd.toByteVector, tl)
            else if size % AES.blockSize == 0 then Pull.pure(Some((acc ++ hd.toByteVector, false, tl)))
            else
              val (pfx, sfx) = hd.splitAt((hd.size - size % AES.blockSize).toInt)
              Pull.pure(Some((acc ++ pfx.toByteVector, false, tl.cons(sfx))))
        }
      in => in.repeatPull(pull => go(ByteVector.empty, pull.echo.stream).flatMap {
        case Some((bv, false, stream)) => Pull.output1(bv).as(Some(stream))
        case Some((bv, _, _)) => Pull.output1(bv).as(None)
        case None => Pull.pure(None)
      })
    end padding

    def unPadding[F[_]]: Pipe[F, ByteVector, Byte] =
      def go(acc: ByteVector, s: Stream[F, ByteVector]): Pull[F, Byte, Option[Stream[F, ByteVector]]] =
        s.pull.uncons.flatMap {
          case None =>
            val padSize: Byte = acc.lastOption.getOrElse(0)
            assert((acc.size - padSize).until(acc.size).forall(acc(_) == padSize))
            Pull.output(Chunk.byteVector(acc.dropRight(padSize))).flatMap(_ => Pull.pure(None))
          case Some((hd, tl)) =>
            val (pfx, sfx) = hd.splitAt(hd.size - 1)
            Pull.output(Chunk.byteVector(acc ++ ByteVector.concat(pfx.iterator)))
              .flatMap(_ => go(ByteVector.concat(sfx.iterator), tl))
        }
      in => in.repeatPull(pull => go(ByteVector.empty, pull.echo.stream))
    end unPadding
  end PKCS5Padding

  val run: IO[Unit] =
    for
      key <- keySpec("1234567890abcdef1234567890abcdef".getBytes(UTF_8))
      ivps <- ivParameterSpec

      message = "Hello, world!" * 50
      encryptedBytes = Stream(message).through(utf8.encode).through(AES.ECB.encrypt(key))
      decryptedBytes = encryptedBytes.through(AES.ECB.decrypt(key))
      encryptedHex = encryptedBytes.through(hex.encode).toList.mkString("")
      decryptedMessage = decryptedBytes.through(utf8.decode).toList.mkString("")

      cbcEncryptedBytes = Stream(message).through(utf8.encode).through(AES.CBC.encrypt(key, ivps))
      cbcDecryptedBytes = cbcEncryptedBytes.through(AES.CBC.decrypt(key, ivps))
      cbcEncryptedHex = cbcEncryptedBytes.through(hex.encode).toList.mkString("")
      cbcDecryptedMessage = cbcDecryptedBytes.through(utf8.decode).toList.mkString("")

      data = message.getBytes(UTF_8)
      javaEncryptedBytes = AES.ECB.javaEncrypt(key.getEncoded, data)
      javaDecryptedBytes = AES.ECB.javaDecrypt(key.getEncoded, javaEncryptedBytes)
      javaEncryptedHex = new BigInteger(1, javaEncryptedBytes).toString(16)
      javaDecryptedMessage = new String(javaDecryptedBytes, UTF_8)

      javaCbcEncryptedBytes = AES.CBC.javaEncrypt(key.getEncoded, ivps, data)
      javaCbcDecryptedBytes = AES.CBC.javaDecrypt(key.getEncoded, ivps, javaCbcEncryptedBytes)
      javaCbcEncryptedHex = new BigInteger(1, javaCbcEncryptedBytes).toString(16)
      javaCbcDecryptedMessage = new String(javaCbcDecryptedBytes, UTF_8)

      _ <- IO.println(s"Data length             : ${data.length}")
      _ <- IO.println(s"EncryptedHex            : $encryptedHex")
      _ <- IO.println(s"JavaEncryptedHex        : $javaEncryptedHex")
      _ <- IO.println(s"CbcEncryptedHex         : $cbcEncryptedHex")
      _ <- IO.println(s"JavaCbcEncryptedHex     : $javaCbcEncryptedHex")
      _ <- IO.println(s"Encrypted Correct       : ${Set(encryptedHex, javaEncryptedHex).size == 1}")
      _ <- IO.println(s"CbcEncrypted Correct    : ${Set(cbcEncryptedHex, javaCbcEncryptedHex).size == 1}")
      _ <- IO.println(s"Message                 : $message")
      _ <- IO.println(s"DecryptedMessage        : $decryptedMessage")
      _ <- IO.println(s"JavaDecryptedMessage    : $javaDecryptedMessage")
      _ <- IO.println(s"CbcDecryptedMessage     : $cbcDecryptedMessage")
      _ <- IO.println(s"JavaCbcDecryptedMessage : $javaCbcDecryptedMessage")
      _ <- IO.println(s"Decrypted Correct       : ${Set(message, decryptedMessage, javaDecryptedMessage).size == 1}")
      _ <- IO.println(s"CbcDecrypted Correct    : ${Set(message, cbcDecryptedMessage, javaCbcDecryptedMessage).size == 1}")
    yield ()
