package com.peknight.demo.oauth2.domain

import cats.data.Validated
import cats.effect.Concurrent
import cats.syntax.apply.*
import cats.syntax.option.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}
import org.typelevel.ci.*

case class OAuthToken(accessToken: String, tokenType: AuthScheme, refreshToken: Option[String],
                      scope: Option[Set[String]], state: Option[String]):

  def toFragment: String = s"$accessTokenKey=$accessToken&$tokenTypeKey=${tokenType.toString}" +
    s"${refreshToken.map(refresh => s"&$refreshTokenKey=$refresh").getOrElse("")}" +
    s"${scope.map(s => s"&$scopeKey=${s.mkString(" ")}").getOrElse("")}" +
    s"${state.map(s => s"&$stateKey=$s").getOrElse("")}"

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct5(
      accessTokenKey,
      tokenTypeKey,
      refreshTokenKey,
      scopeKey,
      stateKey
    )((accessToken: String, tokenType: String, refreshToken: Option[String], scope: Option[String], state: Option[String]) =>
      OAuthToken(accessToken, ci"$tokenType", refreshToken, scope.map(_.split("\\s++").toSet[String]), state)
    )(t => (t.accessToken, t.tokenType.toString, t.refreshToken, t.scope.map(_.mkString(" ")), t.state))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]

  def from(fragment: String): Validated[String, OAuthToken] =
    val params: Map[String, String] = fragment.split("&").map { kv =>
      val pair = kv.split("=", 2)
      pair(0) -> pair(1)
    }.toMap
    (
      params.get(accessTokenKey).toValidNel(accessTokenKey),
      params.get(tokenTypeKey).map(tokenType => ci"$tokenType").toValidNel(tokenTypeKey),
      params.get(refreshTokenKey).validNel[String],
      params.get(scopeKey).map(_.split("\\s++").toSet[String]).validNel[String],
      params.get(stateKey).validNel[String]
    ).mapN(OAuthToken.apply).leftMap(es =>
      es.toList.mkString(s"Invalid param${if es.tail.nonEmpty then "s" else ""}: ", ", ", "")
    )

