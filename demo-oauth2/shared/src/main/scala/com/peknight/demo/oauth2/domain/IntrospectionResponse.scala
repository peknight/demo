package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import com.peknight.demo.oauth2.constant.*
import io.circe.Codec
import io.circe.generic.auto.*
import org.http4s.EntityDecoder
import org.http4s.circe.*

case class IntrospectionResponse(active: Boolean,
                                 scope: Option[Set[String]] = None,
                                 clientId: Option[String] = None,
                                 username: Option[String] = None,
                                 issuer: Option[String] = None,
                                 subject: Option[String] = None,
                                 audience: Option[String] = None,
                                 issuedAt: Option[Long] = None,
                                 expiration: Option[Long] = None,
                                 accessTokenKey: Option[RsaKey] = None)

object IntrospectionResponse:

  given Codec[IntrospectionResponse] =
    Codec.forProduct10(
      activeKey,
      scopeKey,
      clientIdKey,
      usernameKey,
      issuerKey,
      subjectKey,
      audienceKey,
      issuedAtKey,
      expirationKey,
      accessTokenKeyKey
    )((active: Boolean,
       scope: Option[String],
       clientId: Option[String],
       username: Option[String],
       issuer: Option[String],
       subject: Option[String],
       audience: Option[String],
       issuedAt: Option[Long],
       expiration: Option[Long],
       accessTokenKey: Option[RsaKey]) =>
      IntrospectionResponse(active, scope.map(_.split("\\s++").toSet[String]), clientId, username, issuer,
        subject, audience, issuedAt, expiration, accessTokenKey)
    )(t => (t.active, t.scope.map(_.mkString(" ")), t.clientId, t.username, t.issuer, t.subject, t.audience, t.issuedAt,
      t.expiration, t.accessTokenKey))

  given [F[_]: Concurrent]: EntityDecoder[F, IntrospectionResponse] = jsonOf[F, IntrospectionResponse]
