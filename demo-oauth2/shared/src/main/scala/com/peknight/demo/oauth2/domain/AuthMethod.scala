package com.peknight.demo.oauth2.domain

import cats.Show
import io.circe.{Decoder, Encoder}

enum AuthMethod(val value: String) derives CanEqual:
  case SecretBasic extends AuthMethod("secret_basic")
  case SecretPost extends AuthMethod("secret_post")
  case None extends AuthMethod("none")
  case ClientSecretBasic extends AuthMethod("client_secret_basic")
  case ClientSecretPost extends AuthMethod("client_secret_post")

object AuthMethod:
  def fromString(authMethod: String): Option[AuthMethod] = AuthMethod.values.find(_.value == authMethod)

  given Encoder[AuthMethod] = Encoder.encodeString.contramap[AuthMethod](_.value)

  given Decoder[AuthMethod] =
    Decoder.decodeString.emap[AuthMethod](str => fromString(str).toRight(s"No such AuthMethod: $str"))

  given Show[AuthMethod] with
    def show(t: AuthMethod): String = t.value