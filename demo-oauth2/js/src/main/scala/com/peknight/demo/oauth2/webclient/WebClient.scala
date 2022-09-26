package com.peknight.demo.oauth2.webclient

import cats.data.{OptionT, Validated, ValidatedNel}
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.option.*
import cats.syntax.traverse.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.common.UrlFragment.*
import com.peknight.demo.oauth2.common.UrlFragmentDecoder.*
import com.peknight.demo.oauth2.common.{StringCaseStyle, UrlFragment, UrlFragmentDecoder}
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.request.*
import io.circe.Json
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dom.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.scalajs.dom
import org.scalajs.dom.{Event, document}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object WebClient extends IOApp.Simple:

  val run: IO[Unit] =
    for
      _ <- textContent(oauthScopeValueCls)(webClient.scope.mkString(" "))
      _ <- onClick(oauthAuthorizeCls)(_ => handleAuthorizationRequestClick)
      callbackDataR <- Ref.of[IO, Option[OAuthToken]](None)
      _ <- onClick(oauthFetchResourceCls)(_ => handleFetchResourceClick(callbackDataR))
      _ <- onClick(oauthGreetingCls)(_ => handleGreetingClick(callbackDataR))
      _ <- processCallback(callbackDataR)
    yield ()

  private[this] val handleAuthorizationRequestClick: IO[Unit] =
    for
      random <- Random.scalaUtilRandom[IO]
      state <- randomString(random, 32)
      _ <- IO(dom.window.localStorage.setItem(oauthStateKey, state))
      _ <- IO(dom.window.location.href = authServer.authorizationEndpoint.withQueryParams(AuthorizeParam(
        webClient.id, webClient.redirectUris.head, webClient.scope, ResponseType.Token,
        Some(state), None, None
      )).toString)
    yield ()

  private[this] def handleFetchResourceClick(callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    fetchResource(callbackDataR, "Error while fetching the protected resource")(
      oauthToken => resourceRequest(oauthToken.accessToken)
    )

  private[this] def handleGreetingClick(callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    fetchResource(callbackDataR, "Error while greeting")(oauthToken => GET(
      helloWorldApi.withQueryParam("language", "en"),
      Headers(Authorization(Credentials.Token(AuthScheme.Bearer, oauthToken.accessToken)))
    ))

  private[this] def fetchResource(callbackDataR: Ref[IO, Option[OAuthToken]], onError: String)
                   (req: OAuthToken => Request[IO]): IO[Unit] =
    OptionT(callbackDataR.get).flatMap { oauthToken => (
      for
        respEither <- FetchClientBuilder[IO].create.expect[Json](req(oauthToken)).attempt
        text = respEither.fold(_ => onError, _.spaces4)
        _ <- textContent(oauthProtectedResourceCls)(text)
      yield ()
    ).optionT }.value.void

  private[this] def processCallback(callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    val processOptionT: OptionT[IO, Unit] = for
      hash <- IO(dom.window.location.hash).optionT if hash.nonEmpty
      oauthToken <- OptionT(parseOAuthToken(Uri.decode(hash.substring(1)))
        .fold[IO[Option[OAuthToken]]](msg => IO.println(msg) *> IO.pure(None), oauthToken => IO.pure(oauthToken.some)))
      _ <- checkStateAndUpdateCallbackData(oauthToken, callbackDataR).optionT
    yield ()
    processOptionT.value.void
  end processCallback

  private[this] def parseOAuthToken(fragment: String): Validated[String, OAuthToken] =
    given UrlFragmentDecoder[Set[String]] with
      def decode(fragment: UrlFragment): ValidatedNel[String, Set[String]] = fragment match
        case UrlFragmentValue(value) => value.split("\\s++").toSet.validNel[String]
        case fragment => s"Can not parse $fragment".invalidNel[Set[String]]
    end given
    fragment.parseFragment[OAuthToken](StringCaseStyle.snakeToCamel).leftMap(es => es.toList.mkString(" >> "))
  end parseOAuthToken

  private[this] def checkStateAndUpdateCallbackData(oauthToken: OAuthToken, callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    for
      localStateOption <- IO(Option(dom.window.localStorage.getItem(oauthStateKey)))
      _ <- localStateOption.filter(oauthToken.state.contains)
        .fold[IO[Unit]](stateNotMatch(localStateOption, oauthToken.state, callbackDataR)) { _ =>
          updateCallbackData(oauthToken, callbackDataR)
        }
    yield ()

  private[this] def stateNotMatch(localStateOption: Option[String], callbackStateOption: Option[String],
                    callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    val localState = localStateOption.getOrElse("None")
    val callbackState = callbackStateOption.getOrElse("None")
    for
      _ <- IO.println(s"State DOES NOT MATCH: expected $localState got $callbackState")
      _ <- callbackDataR.set(None)
      _ <- textContent(oauthProtectedResourceCls)("Error state value did not match")
    yield ()

  private[this] def updateCallbackData(oauthToken: OAuthToken, callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    for
      _ <- callbackDataR.set(Some(oauthToken))
      _ <- textContent(oauthAccessTokenCls)(oauthToken.accessToken)
      _ <- IO.println(s"access_token: ${oauthToken.accessToken}")
    yield ()

  private[this] def onClick[T <: Event](cls: String)(f: T => IO[Unit]): IO[Unit] =
    traverseElements(cls)(element => addEventListener(element)("click")(f)).void

  private[this] def addEventListener[T <: Event, A](target: dom.EventTarget)(`type`: String)(f: T => IO[A]): IO[Unit] =
    IO(target.addEventListener(`type`, (event: T) => f(event).run()))

  private[this] def textContent(cls: String)(text: String): IO[Unit] =
    traverseElements(cls)(element => IO(element.textContent = text)).void

  private[this] def traverseElements[A](cls: String)(f: dom.Element => IO[A]): IO[List[A]] =
    for
      elements <- IO(document.getElementsByClassName(cls))
      res <- dom.DOMList.domListAsSeq(elements).toList.traverse(f)
    yield res

  extension[A] (io: IO[A])
    def run(): Unit =
      import cats.effect.unsafe.implicits.global
      io.unsafeRunAndForget()
  end extension
