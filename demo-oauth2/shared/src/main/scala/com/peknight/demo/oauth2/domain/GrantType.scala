package com.peknight.demo.oauth2.domain

import io.circe.{Decoder, Encoder}

enum GrantType(val value: String) derives CanEqual:
  case AuthorizationCode extends GrantType("authorization_code")
  case RefreshToken extends GrantType("refresh_token")
  case ClientCredentials extends GrantType("client_credentials")
  case Password extends GrantType("password")

object GrantType:
  def fromString(grantType: String): Option[GrantType] = GrantType.values.find(_.value == grantType)

  given Encoder[GrantType] = Encoder.encodeString.contramap[GrantType](_.value)

  given Decoder[GrantType] =
    Decoder.decodeString.emap[GrantType](str => fromString(str).toRight(s"No such GrantType: $str"))
