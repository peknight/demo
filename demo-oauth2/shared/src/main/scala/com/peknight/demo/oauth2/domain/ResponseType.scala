package com.peknight.demo.oauth2.domain

import cats.Show
import io.circe.{Decoder, Encoder}

enum ResponseType(val value: String) derives CanEqual:
  // 授权码响应类型，该类型会返回授权码，该授权码会被传递至令牌端点用于获取令牌
  case Code extends ResponseType("code")
  // 隐式响应类型，该类型会直接向重定向URI返回令牌
  case Token extends ResponseType("token")

object ResponseType:
  def fromString(responseType: String): Option[ResponseType] = ResponseType.values.find(_.value == responseType)

  given Encoder[ResponseType] = Encoder.encodeString.contramap[ResponseType](_.value)

  given Decoder[ResponseType] =
    Decoder.decodeString.emap[ResponseType](str => fromString(str).toRight(s"No such ResponseType: $str"))

  given Show[ResponseType] with
    def show(t: ResponseType): String = t.value