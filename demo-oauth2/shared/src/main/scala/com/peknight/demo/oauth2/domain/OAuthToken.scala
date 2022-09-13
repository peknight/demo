package com.peknight.demo.oauth2.domain

import cats.data.{Validated, ValidatedNel}
import cats.effect.Concurrent
import cats.syntax.apply.*
import cats.syntax.option.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.common.UrlFragment.*
import com.peknight.demo.oauth2.common.{UrlFragment, UrlFragmentDecoder}
import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{AuthScheme, EntityDecoder}
import org.typelevel.ci.*

case class OAuthToken(accessToken: String, tokenType: AuthScheme, refreshToken: Option[String],
                      scope: Option[Set[String]], state: Option[String], idToken: Option[String])

object OAuthToken:

  given Codec[OAuthToken] =
    Codec.forProduct6(
      accessTokenKey,
      tokenTypeKey,
      refreshTokenKey,
      scopeKey,
      stateKey,
      idTokenKey
    )((accessToken: String,
       tokenType: String,
       refreshToken: Option[String],
       scope: Option[String],
       state: Option[String],
       idToken: Option[String]) =>
      OAuthToken(accessToken, ci"$tokenType", refreshToken, scope.map(_.split("\\s++").toSet[String]), state,
        idToken)
    )(t => (t.accessToken, t.tokenType.toString, t.refreshToken, t.scope.map(_.mkString(" ")), t.state, t.idToken))

  given [F[_]: Concurrent]: EntityDecoder[F, OAuthToken] = jsonOf[F, OAuthToken]

  given UrlFragmentDecoder[OAuthToken] with
    def decode(fragment: UrlFragment): ValidatedNel[String, OAuthToken] = fragment match
      case UrlFragmentNone => "UrlFragmentNone".invalidNel[OAuthToken]
      case UrlFragmentValue(value) => s"UrlFragmentValue($value)".invalidNel[OAuthToken]
      case UrlFragmentObject(map) =>
        def get(key: String): Option[String] = map.get(key).flatMap {
          case UrlFragmentValue(value) => value.some
          case _ => none[String]
        }
        (
          get(accessTokenKey).toValidNel(accessTokenKey),
          get(tokenTypeKey).map(tokenType => ci"$tokenType").toValidNel(tokenTypeKey),
          get(refreshTokenKey).validNel[String],
          get(scopeKey).map(_.split("\\s++").toSet[String]).validNel[String],
          get(stateKey).validNel[String],
          get(idTokenKey).validNel[String]
        ).mapN(OAuthToken.apply)
    end decode