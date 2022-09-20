package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import cats.effect.Concurrent
import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{EntityDecoder, Uri}

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

object ClientInfo:
  given Codec[ClientInfo] = Codec.forProduct14(
    clientIdKey,
    clientSecretKey,
    scopeKey,
    redirectUrisKey,
    clientNameKey,
    clientUriKey,
    logoUriKey,
    tokenEndpointAuthMethodKey,
    grantTypesKey,
    responseTypesKey,
    clientIdCreatedAtKey,
    clientSecretExpiresAtKey,
    registrationAccessTokenKey,
    registrationClientUriKey
  )((clientId: String,
    clientSecret: Option[String],
    scope: Option[String],
    redirectUris: NonEmptyList[Uri],
    clientName: Option[String],
    clientUri: Option[Uri],
    logoUri: Option[Uri],
    tokenEndpointAuthMethod: Option[AuthMethod],
    grantTypes: Option[List[GrantType]],
    responseTypes: Option[List[ResponseType]],
    clientIdCreatedAt: Option[Long],
    clientSecretExpiresAt: Option[Long],
    registrationAccessToken: Option[String],
    registrationClientUri: Option[Uri]
  ) => ClientInfo(clientId, clientSecret, scope.map(_.split("\\s++").toSet[String]).getOrElse(Set.empty[String]),
    redirectUris, clientName, clientUri, logoUri, tokenEndpointAuthMethod, grantTypes, responseTypes, clientIdCreatedAt,
    clientSecretExpiresAt, registrationAccessToken, registrationClientUri)
  )(t => (t.id, t.secret, Some(t.scope.mkString(" ")), t.redirectUris, t.name, t.uri, t.logoUri,
    t.tokenEndpointAuthMethod, t.grantTypes, t.responseTypes, t.clientIdCreatedAt, t.clientSecretExpiresAt,
    t.registrationAccessToken, t.registrationClientUri))

  given [F[_]: Concurrent]: EntityDecoder[F, ClientInfo] = jsonOf[F, ClientInfo]
