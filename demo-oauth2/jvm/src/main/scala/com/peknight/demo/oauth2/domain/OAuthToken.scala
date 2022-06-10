package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}
import org.typelevel.ci.*

case class OAuthToken(accessToken: String, tokenType: AuthScheme, refreshToken: Option[String], scope: Option[Set[String]])

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct4(
      "access_token",
      "token_type",
      "refresh_token",
      "scope"
    )((accessToken: String, tokenType: String, refreshToken: Option[String], scope: Option[String]) =>
      OAuthToken(accessToken, ci"$tokenType", refreshToken, scope.map(_.split("\\s++").toSet[String]))
    )(t => (t.accessToken, t.tokenType.toString, t.refreshToken, t.scope.map(_.mkString(" "))))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]
