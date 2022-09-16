package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import cats.effect.Concurrent
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.domain
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{EntityDecoder, Uri}

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

end ClientMetadata

object ClientMetadata:
  given Codec[ClientMetadata] =
    Codec.forProduct8(
      tokenEndpointAuthMethodKey,
      grantTypesKey,
      responseTypesKey,
      redirectUrisKey,
      clientNameKey,
      clientUriKey,
      logoUriKey,
      scopeKey
    )((tokenEndpointAuthMethod: AuthMethod,
       grantTypes: List[GrantType],
       responseTypes: List[ResponseType],
       redirectUris: NonEmptyList[Uri],
       clientName: Option[String],
       clientUri: Option[Uri], logoUri: Option[Uri], scope: Option[String]) =>
      ClientMetadata(tokenEndpointAuthMethod, grantTypes, responseTypes, redirectUris, clientName, clientUri, logoUri,
        scope.map(_.split("\\s++").toSet[String]))
    )(t => (t.tokenEndpointAuthMethod, t.grantTypes, t.responseTypes, t.redirectUris, t.clientName, t.clientUri,
      t.logoUri, t.scope.map(_.mkString(" "))))
  given [F[_]: Concurrent]: EntityDecoder[F, ClientMetadata] = jsonOf[F, ClientMetadata]
end ClientMetadata
