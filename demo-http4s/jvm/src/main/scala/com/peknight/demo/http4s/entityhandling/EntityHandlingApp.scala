package com.peknight.demo.http4s.entityhandling

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.http4s.entityhandling.Resp.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.headers.`Content-Type`

object EntityHandlingApp extends IOApp.Simple:

  val audioResponse = Ok("").map(_.withContentType(`Content-Type`(MediaType.audio.ogg)))

  val videoResponse = Ok("").map(_.withContentType(`Content-Type`(MediaType.video.ogg)))

  val audioDec = EntityDecoder.decodeBy(MediaType.audio.ogg) { (m: Media[IO]) =>
    EitherT { m.as[String].map(s => Audio(s).asRight[DecodeFailure]) }
  }

  val videoDec = EntityDecoder.decodeBy(MediaType.video.ogg) { (m: Media[IO]) =>
    EitherT { m.as[String].map(s => Video(s).asRight[DecodeFailure]) }
  }

  given bothDec: EntityDecoder[IO, Resp] = audioDec.widen[Resp] orElse videoDec.widen[Resp]

  def run =
    for
      audioResp <- audioResponse
      audio <- audioResp.as[Resp]
      _ <- IO.println(audio)
      videoResp <- videoResponse
      video <- videoResp.as[Resp]
      _ <- IO.println(video)
    yield ()