package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}
import org.typelevel.ci.*

case class OAuthToken(accessToken: String, tokenType: AuthScheme, refreshToken: Option[String],
                      scope: Option[Set[String]], state: Option[String]):
  def toFragment: String = s"access_token=$accessToken&tokenType=${tokenType.toString}" +
    s"${refreshToken.map(refresh => s"&refresh_token=$refresh").getOrElse("")}" +
    s"${scope.map(s => s"&scope=${s.mkString(" ")}").getOrElse("")}" +
    s"${state.map(s => s"&state=$s").getOrElse("")}"

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct5(
      "access_token",
      "token_type",
      "refresh_token",
      "scope",
      "state"
    )((accessToken: String, tokenType: String, refreshToken: Option[String], scope: Option[String], state: Option[String]) =>
      OAuthToken(accessToken, ci"$tokenType", refreshToken, scope.map(_.split("\\s++").toSet[String]), state)
    )(t => (t.accessToken, t.tokenType.toString, t.refreshToken, t.scope.map(_.mkString(" ")), t.state))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]
