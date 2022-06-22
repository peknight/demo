package com.peknight.demo.security.cipher

import cats.effect.{IO, IOApp}
import fs2.text.{base64, hex, utf8}
import fs2.{Chunk, INothing, Pipe, Pull, Stream}

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

  def chunkTimesN[F[_], O](n: Int): Pipe[F, O, Chunk[O]] =
    def go(acc: Chunk[O], s: Stream[F, O]): Pull[F, INothing, Option[(Chunk[O], Stream[F, O])]] =
      s.pull.uncons.flatMap {
        case None =>
          if acc.isEmpty then
            println(s"chunkTimesN($n) None acc.isEmpty")
            Pull.pure(None)
          else
            println(s"chunkTimesN($n) None acc = $acc")
            Pull.pure(Some((acc, Stream.empty)))
        case Some((hd, tl)) =>
          val size = acc.size + hd.size
          if size < n then
            println(s"chunkTimesN($n) Some(($hd, $tl)) acc = $acc $size < $n >> go")
            go(acc ++ hd, tl)
          else if size % n == 0 then
            println(s"chunkTimesN($n) Some(($hd, $tl)) acc = $acc $size % $n == 0")
            Pull.pure(Some((acc ++ hd) -> tl))
          else
            val (pfx, sfx) = hd.splitAt(hd.size - size % n)
            println(s"chunkTimesN($n) Some(($hd, $tl)) acc = $acc $size % $n != 0 pfx = $pfx sfx = $sfx")
            Pull.pure(Some((acc ++ pfx) -> tl.cons(sfx)))
      }
    in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream).flatMap {
      case Some((c, s)) =>
        println(s"chunkTimesN($n) repeatPull Some(($c, $s))")
        Pull.output1(c).as(Some(s))
      case None =>
        println(s"chunkTimesN($n) repeatPull None")
        Pull.pure(None)
    })
  end chunkTimesN

  object AES:
    val blockSize = 16
    object ECB:
      private[this] val transformation = "AES/ECB/NoPadding"

      def encrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.through(PKCS5Padding.padding).flatMap { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.ENCRYPT_MODE, key)
          val output = cipher.doFinal(chunk.toArray)
          Stream.chunk(Chunk.array(output))
        }

      def decrypt[F[_]](key: => SecretKey): Pipe[F, Byte, Byte] = in =>
        in.through(chunkTimesN(blockSize)).map { chunk =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key)
          val output = cipher.doFinal(chunk.toArray)
          Chunk.array(output)
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
          val output = cipher.doFinal(chunks.flatMap(identity).toArray)
          val outputChunk = Chunk.array(output)
          val nextIv: Array[Byte] = outputChunk.splitAt(outputChunk.size - blockSize)._2.toArray
          val nextIvps: IvParameterSpec = new IvParameterSpec(nextIv)
          println(s"encrypt key = ${Chunk.array(key.getEncoded)} ivps = ${Chunk.array(ivps.getIV)} input = $chunks output = $outputChunk nextIvps = ${Chunk.array(nextIvps.getIV)}")
          (nextIvps, outputChunk)
        }

      def decrypt[F[_]](key: => SecretKey, ivps: => IvParameterSpec): Pipe[F, Byte, Byte] = in =>
        in.through(chunkTimesN(blockSize)).scanChunks(ivps) { (ivps, chunks) =>
          val cipher: Cipher = Cipher.getInstance(transformation)
          cipher.init(Cipher.DECRYPT_MODE, key, ivps)
          val inputChunk = chunks.flatMap(identity)
          val output = cipher.doFinal(inputChunk.toArray)
          val outputChunk = Chunk.array(output)
          val nextIv: Array[Byte] = inputChunk.splitAt(inputChunk.size - blockSize)._2.toArray
          val nextIvps: IvParameterSpec = new IvParameterSpec(nextIv)
          println(s"decrypt key = ${Chunk.array(key.getEncoded)} ivps = ${Chunk.array(ivps.getIV)} input = $chunks output = $outputChunk nextIvps = ${Chunk.array(nextIvps.getIV)}")
          (nextIvps, Chunk(outputChunk))
        }.through(PKCS5Padding.unPadding)

      def javaEncrypt(key: Array[Byte], ivps: IvParameterSpec, input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps)
        val output: Array[Byte] = cipher.doFinal(input)
        println(s"javaEncrypt key = ${Chunk.array(key)} ivps = ${Chunk.array(ivps.getIV)} input = ${Chunk.array(input)} output = ${Chunk.array(output)}")
        output

      def javaDecrypt(key: Array[Byte], ivps: IvParameterSpec, input: Array[Byte]): Array[Byte] =
        val cipher: Cipher = Cipher.getInstance(javaTransformation)
        val keySpec: SecretKey = new SecretKeySpec(key, aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivps)
        val output: Array[Byte] = cipher.doFinal(input)
        println(s"javaDecrypt key = ${Chunk.array(key)} ivps = ${Chunk.array(ivps.getIV)} input = ${Chunk.array(input)} output = ${Chunk.array(output)}")
        output

    end CBC
  end AES

  object PKCS5Padding:
    def padding[F[_]]: Pipe[F, Byte, Chunk[Byte]] =
      def go(acc: Chunk[Byte], s: Stream[F, Byte]): Pull[F, INothing, Option[(Chunk[Byte], Boolean, Stream[F, Byte])]] =
        s.pull.uncons.flatMap {
          case None =>
            val paddingSize = AES.blockSize - acc.size
            println(s"padding None acc = $acc paddingSize = $paddingSize")
            Pull.pure(Some((acc ++ Chunk.seq(List.fill(paddingSize)(paddingSize.toByte)), true, Stream.empty)))
          case Some((hd, tl)) =>
            val size = acc.size + hd.size
            if size < AES.blockSize then
              println(s"padding Some(($hd, $tl)) acc = $acc $size < ${AES.blockSize} >> go")
              go(acc ++ hd, tl)
            else if size % AES.blockSize == 0 then
              println(s"padding Some(($hd, $tl)) acc = $acc $size % ${AES.blockSize} == 0")
              Pull.pure(Some((acc ++ hd, false, tl)))
            else
              val (pfx, sfx) = hd.splitAt(hd.size - size % AES.blockSize)
              println(s"padding Some(($hd, $tl)) acc = $acc $size % ${AES.blockSize} != 0 pfx = $pfx sfx = $sfx")
              Pull.pure(Some((acc ++ pfx, false, tl.cons(sfx))))
        }
      in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream).flatMap {
        case Some((chunk, false, stream)) =>
          println(s"padding repeatPull Some(($chunk, false, _))")
          Pull.output1(chunk).as(Some(stream))
        case Some((chunk, _, _)) =>
          println(s"padding repeatPull Some(($chunk, true, _))")
          Pull.output1(chunk).as(None)
        case None =>
          println(s"padding repeatPull None")
          Pull.pure(None)
      })
    end padding

    def unPadding[F[_]]: Pipe[F, Chunk[Byte], Byte] =
      def go(acc: Chunk[Byte], s: Stream[F, Chunk[Byte]]): Pull[F, Byte, Option[Stream[F, Chunk[Byte]]]] =
        s.pull.uncons.flatMap {
          case None =>
            val padSize: Byte = acc.last.getOrElse(0)
            println(s"unPadding None acc = $acc padSize = $padSize")
            assert((acc.size - padSize).until(acc.size).forall(acc(_) == padSize))
            Pull.output(acc.dropRight(padSize)).flatMap(_ => Pull.pure(None))
          case Some((hd, tl)) =>
            val (pfx, sfx) = hd.splitAt(hd.size - 1)
            println(s"unPadding Some(($hd, $tl)) acc = $acc pfx = $pfx sfx = $sfx")
            Pull.output(acc ++ pfx.flatMap(identity)).flatMap(_ => go(sfx.flatMap(identity), tl))
        }
      in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream))
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
