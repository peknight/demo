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

  def updateClientInfo(client: ClientInfo): ClientInfo =
    ClientInfo(client.id, client.secret, scope.getOrElse(client.scope), redirectUris, clientName.orElse(client.name),
      clientUri.orElse(client.uri), logoUri.orElse(client.logoUri), Some(tokenEndpointAuthMethod), Some(grantTypes),
      Some(responseTypes), client.clientIdCreatedAt, client.clientSecretExpiresAt, client.registrationAccessToken,
      client.registrationClientUri)
