package com.peknight.demo.oauth2.domain

import cats.Show
import io.circe.{Decoder, Encoder}

enum ResponseType(val value: String) derives CanEqual:
  case Code extends ResponseType("code")
  case Token extends ResponseType("token")

object ResponseType:
  def fromString(responseType: String): Option[ResponseType] = ResponseType.values.find(_.value == responseType)

  given Encoder[ResponseType] = Encoder.encodeString.contramap[ResponseType](_.value)

  given Decoder[ResponseType] =
    Decoder.decodeString.emap[ResponseType](str => fromString(str).toRight(s"No such ResponseType: $str"))

  given Show[ResponseType] with
    def show(t: ResponseType): String = t.value