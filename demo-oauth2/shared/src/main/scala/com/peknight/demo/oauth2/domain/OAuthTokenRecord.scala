package com.peknight.demo.oauth2.domain

import io.circe.Codec

case class OAuthTokenRecord(clientId: String, accessToken: Option[String], refreshToken: Option[String],
                            scope: Option[Set[String]], user: Option[String])

object OAuthTokenRecord:
  given Codec[OAuthTokenRecord] = Codec.forProduct5(
    "client_id",
    "access_token",
    "refresh_token",
    "scope",
    "user"
  )(
    (
      clientId: String,
      accessToken: Option[String],
      refreshToken: Option[String],
      scopeStr: Option[String],
      user: Option[String]
    ) =>
      OAuthTokenRecord(clientId, accessToken, refreshToken, scopeStr.map(_.split("\\s++").toSet[String]), user)
  )(t => (t.clientId, t.accessToken, t.refreshToken, t.scope.map(_.mkString(" ")), t.user))
