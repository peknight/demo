package com.peknight.demo.oauth2.client

import cats.data.NonEmptyList
import org.http4s.Uri

case class ClientInfo(id: String, secret: String, redirectUris: NonEmptyList[Uri], scope: String)
