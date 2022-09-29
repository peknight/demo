package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import io.circe.generic.auto.*
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}
import org.typelevel.ci.*

case class OAuthToken(accessToken: String, tokenType: AuthScheme, refreshToken: Option[String],
                      scope: Option[Set[String]], state: Option[String], idToken: Option[String],
                      accessTokenKey: Option[RsaKey], algorithm: Option[String])

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct8(
      accessTokenKey,
      tokenTypeKey,
      refreshTokenKey,
      scopeKey,
      stateKey,
      idTokenKey,
      accessTokenKeyKey,
      algorithmKey
    )((accessToken: String,
       tokenType: String,
       refreshToken: Option[String],
       scope: Option[String],
       state: Option[String],
       idToken: Option[String],
       accessTokenKey: Option[RsaKey],
       algorithm: Option[String]) =>
      OAuthToken(accessToken, ci"$tokenType", refreshToken, scope.map(_.split("\\s++").toSet[String]), state,
        idToken, accessTokenKey, algorithm)
    )(t => (t.accessToken, t.tokenType.toString, t.refreshToken, t.scope.map(_.mkString(" ")), t.state, t.idToken,
      t.accessTokenKey, t.algorithm))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]