package com.peknight.demo.oauth2.domain

import cats.data.Validated
import cats.syntax.apply.*
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.domain.AuthorizeParam.{clientIdKey, redirectUriKey, responseTypeKey, scopeKey, stateKey}
import org.http4s.Uri

case class AuthorizeParam(clientId: String, redirectUri: Uri, scope: Set[String], responseType: ResponseType,
                          state: Option[String])

object AuthorizeParam:

  val clientIdKey = "client_id"
  val redirectUriKey = "redirect_uri"
  val scopeKey = "scope"
  val responseTypeKey = "response_type"
  val stateKey = "state"

  def unapply(params: Map[String, collection.Seq[String]]): Some[Validated[String, AuthorizeParam]] =
    Some {
      (
        params.get(clientIdKey).flatMap(_.find(_.nonEmpty)).toValidNel(clientIdKey),
        params.get(redirectUriKey).flatMap { uriSeq =>
          val uris = uriSeq.to(LazyList).map(Uri.fromString)
          uris.find(_.isRight).orElse(uris.headOption).map(_.leftMap(_ => redirectUriKey).toValidatedNel)
        }.getOrElse(redirectUriKey.invalidNel[Uri]),
        params.get(scopeKey).map(_.flatMap(_.split("\\s++").toSeq).to(Set)).toValidNel(scopeKey),
        params.get(responseTypeKey).flatMap(_.find(_.nonEmpty)).flatMap(ResponseType.fromString)
          .toValidNel(responseTypeKey),
        params.get(stateKey).flatMap(_.find(_.nonEmpty)).validNel[String]
      ).mapN(AuthorizeParam.apply).leftMap(es =>
        es.toList.mkString(s"Invalid param${if es.tail.nonEmpty then "s" else ""}: ", ", ", "")
      )
    }

  extension (authorizationEndPoint: Uri)
    def withQueryParams(authorizeParam: AuthorizeParam): Uri = authorizationEndPoint
      .withQueryParams(Map(
        responseTypeKey -> authorizeParam.responseType.value,
        scopeKey -> authorizeParam.scope.mkString(" "),
        clientIdKey -> authorizeParam.clientId))
      .withOptionQueryParam(stateKey, authorizeParam.state)
      .withQueryParam(redirectUriKey, authorizeParam.redirectUri)
