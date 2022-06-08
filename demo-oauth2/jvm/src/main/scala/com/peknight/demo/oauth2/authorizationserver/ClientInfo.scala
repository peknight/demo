package com.peknight.demo.oauth2.authorizationserver

import org.http4s.Uri

case class ClientInfo(id: String, secret: String, scope: List[String], redirectUris: List[Uri])
