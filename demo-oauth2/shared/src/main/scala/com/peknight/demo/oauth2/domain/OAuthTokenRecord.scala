package com.peknight.demo.oauth2.domain

import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import io.circe.generic.auto.*

case class OAuthTokenRecord(clientId: Option[String], accessToken: Option[String], refreshToken: Option[String],
                            scope: Option[Set[String]], user: Option[String], accessTokenKey: Option[RsaKey])

object OAuthTokenRecord:
  given Codec[OAuthTokenRecord] = Codec.forProduct6(
    clientIdKey,
    accessTokenKey,
    refreshTokenKey,
    scopeKey,
    userKey,
    accessTokenKeyKey
  )(
    (
      clientId: Option[String],
      accessToken: Option[String],
      refreshToken: Option[String],
      scopeStr: Option[String],
      user: Option[String],
      accessTokenKey: Option[RsaKey]
    ) =>
      OAuthTokenRecord(clientId, accessToken, refreshToken, scopeStr.map(_.split("\\s++").toSet[String]), user,
        accessTokenKey)
  )(t => (t.clientId, t.accessToken, t.refreshToken, t.scope.map(_.mkString(" ")), t.user, t.accessTokenKey))
