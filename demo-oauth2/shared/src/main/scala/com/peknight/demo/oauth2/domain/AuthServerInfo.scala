package com.peknight.demo.oauth2.domain

import org.http4s.Uri
import org.http4s.syntax.literals.uri

case class AuthServerInfo(authorizationEndpoint: Uri, tokenEndpoint: Uri, revocationEndpoint: Uri,
                          registrationEndpoint: Uri, userInfoEndpoint: Uri, introspectionEndpoint: Uri)

object AuthServerInfo:
  val authServer: AuthServerInfo = AuthServerInfo(
    uri"http://localhost:8001/authorize",
    uri"http://localhost:8001/token",
    uri"http://localhost:8001/revoke",
    uri"http://localhost:8001/register",
    uri"http://localhost:8001/userinfo",
    uri"http://localhost:8001/introspect"
  )
