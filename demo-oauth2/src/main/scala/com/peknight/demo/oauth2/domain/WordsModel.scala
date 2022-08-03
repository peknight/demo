package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}

case class WordsModel(words: Seq[String], timestamp: Long, result: Option[WordsResult])

object WordsModel:
  given Codec[WordsModel] = Codec.forProduct3(
    "words",
    "timestamp",
    "result"
  )((words: String, timestamp: Long, result: Option[String]) => WordsModel(words.split("\\s++").toSeq.filter(_.nonEmpty), timestamp, result.flatMap(WordsResult.fromString))
  )(t => (t.words.mkString(" "), t.timestamp, t.result.map(_.value)))

  given [F[_]: Concurrent]: EntityDecoder[F, WordsModel] = jsonOf[F, WordsModel]
