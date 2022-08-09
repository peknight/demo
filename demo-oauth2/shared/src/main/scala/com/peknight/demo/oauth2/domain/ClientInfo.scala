package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import org.http4s.Uri
import org.http4s.syntax.literals.uri

case class ClientInfo(id: String, secret: String, scope: Set[String], redirectUris: NonEmptyList[Uri],
                      name: Option[String] = None, uri: Option[String] = None, logoUri: Option[String] = None)

object ClientInfo:
  val client: ClientInfo = ClientInfo(
    "oauth-client-1",
    "oauth-client-secret-1",
    Set("foo", "bar", "read", "write", "delete", "fruit", "veggies", "meats", "movies", "foods", "music"),
    NonEmptyList.one(uri"http://localhost:8000/callback")
  )
