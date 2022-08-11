package com.peknight.demo.oauth2.domain

import org.http4s.Uri

case class IdToken(iss: Uri, sub: String, scope: Set[String], aud: Uri, iat: Long, exp: Long, jti: String)
