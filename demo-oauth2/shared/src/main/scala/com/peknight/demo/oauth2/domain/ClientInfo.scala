package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import org.http4s.Uri

case class ClientInfo(id: String,
                      secret: Option[String],
                      scope: Set[String],
                      redirectUris: NonEmptyList[Uri],
                      name: Option[String] = None,
                      uri: Option[Uri] = None,
                      logoUri: Option[Uri] = None,
                      tokenEndpointAuthMethod: Option[AuthMethod] = None,
                      grantTypes: Option[List[GrantType]] = None,
                      responseTypes: Option[List[ResponseType]] = None,
                      clientIdCreatedAt: Option[Long] = None,
                      clientSecretExpiresAt: Option[Long] = None,
                      registrationAccessToken: Option[String] = None,
                      registrationClientUri: Option[Uri] = None)