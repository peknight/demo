package com.peknight.demo.oauth2.client

import cats.effect.Concurrent
import io.circe.Codec
import org.http4s.EntityDecoder
import org.http4s.circe.*

case class OAuthToken(accessToken: String, tokenType: String, refreshToken: Option[String], scope: Option[String])

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct4(
      "access_token",
      "token_type",
      "refresh_token",
      "scope"
    )(OAuthToken.apply)(t => (t.accessToken, t.tokenType, t.refreshToken, t.scope))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]
