package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import org.http4s.Uri

case class ClientMetadata(tokenEndpointAuthMethod: AuthMethod,
                          grantTypes: List[GrantType],
                          responseTypes: List[ResponseType],
                          redirectUris: NonEmptyList[Uri],
                          clientName: Option[String],
                          clientUri: Option[Uri],
                          logoUri: Option[Uri],
                          scope: Option[Set[String]]):
  def toClientInfo(clientId: String, clientSecret: Option[String], clientIdCreatedAt: Long,
                   clientSecretExpiresAt: Long, registrationAccessToken: String,
                   registrationClientUri: Uri): ClientInfo =
    ClientInfo(clientId, clientSecret, scope.getOrElse(Set.empty[String]), redirectUris, clientName, clientUri, logoUri,
      Some(tokenEndpointAuthMethod), Some(grantTypes), Some(responseTypes), Some(clientIdCreatedAt),
      Some(clientSecretExpiresAt), Some(registrationAccessToken), Some(registrationClientUri))
