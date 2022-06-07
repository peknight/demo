package com.peknight.demo.oauth2.protectedresource

import io.circe.Codec

case class OAuthTokenRecord(clientId: String, accessToken: Option[String], refreshToken: Option[String],
                            scope: Option[List[String]])

object OAuthTokenRecord:
  given Codec[OAuthTokenRecord] = Codec.forProduct4(
    "client_id",
    "access_token",
    "refresh_token",
    "scope"
  )(
    (clientId: String, accessToken: Option[String], refreshToken: Option[String], scopeStr: Option[String]) =>
      OAuthTokenRecord(clientId, accessToken, refreshToken, scopeStr.map(_.split("\\s++").toList))
  )(t => (t.clientId, t.accessToken, t.refreshToken, t.scope.map(_.mkString(" "))))
