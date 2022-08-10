package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import org.http4s.Uri

case class ClientInfo(id: String, secret: String, scope: Set[String], redirectUris: NonEmptyList[Uri],
                      name: Option[String] = None, uri: Option[String] = None, logoUri: Option[String] = None)