package com.peknight.demo.oauth2.protectedresource

import io.circe.Codec

case class OAuthTokenRecord(clientId: String, accessToken: Option[String], refreshToken: Option[String],
                            scope: Option[String])

object OAuthTokenRecord:
  given Codec[OAuthTokenRecord] = Codec.forProduct4(
    "client_id",
    "access_token",
    "refresh_token",
    "scope"
  )(OAuthTokenRecord.apply)(t => (t.clientId, t.accessToken, t.refreshToken, t.scope))
