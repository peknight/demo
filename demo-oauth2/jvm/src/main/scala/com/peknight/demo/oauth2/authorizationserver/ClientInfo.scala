package com.peknight.demo.oauth2.authorizationserver

import org.http4s.Uri

case class ClientInfo(id: String, secret: String, scope: Set[String], redirectUris: List[Uri],
                      name: Option[String] = None, uri: Option[String] = None, logoUri: Option[String] = None)
