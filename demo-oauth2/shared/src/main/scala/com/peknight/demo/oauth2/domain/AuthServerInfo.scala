package com.peknight.demo.oauth2.domain

import org.http4s.Uri

case class AuthServerInfo(authorizationEndpoint: Uri, tokenEndpoint: Uri, revocationEndpoint: Uri,
                          registrationEndpoint: Uri, userInfoEndpoint: Uri, introspectionEndpoint: Uri)