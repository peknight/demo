package com.peknight.demo.oauth2.domain

import org.http4s.Uri
import org.http4s.syntax.literals.uri

case class AuthServerInfo(authorizationEndpoint: Uri = uri"http://localhost:8001/authorize",
                          tokenEndpoint: Uri = uri"http://localhost:8001/token",
                          revocationEndpoint: Uri = uri"http://localhost:8001/revoke",
                          registrationEndpoint: Uri = uri"http://localhost:8001/register",
                          userInfoEndpoint: Uri = uri"http://localhost:8001/userinfo"
                         )
