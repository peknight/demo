package com.peknight.demo.oauth2.webclient

import cats.effect.std.Random
import cats.effect.{IO, Ref}
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.peknight.demo.oauth2.constant.*
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

object WebClient:

  def main(args: Array[String]): Unit =
    val io: IO[Unit] = for
      _ <- textContent(oauthScopeValueCls)(webClient.scope.mkString(" "))
      _ <- onClick(oauthAuthorizeCls)(_ => handleAuthorizationRequestClick().run())
      callbackDataR <- Ref.of[IO, Option[OAuthToken]](None)
      _ <- onClick(oauthFetchResourceCls)(_ => handleFetchResourceClick(callbackDataR).run())
      hashOption <- IO(Option(dom.window.location.hash))
      _ <- hashOption.fold(IO.unit)(_ => processCallback(callbackDataR))
    yield ()
    io.run()
  end main


  def handleAuthorizationRequestClick(): IO[Unit] =
    for
      random <- Random.scalaUtilRandom[IO]
      state <- randomString(random, 32)
      _ <- IO(dom.window.localStorage.setItem(oauthStateKey, state))
      _ <- IO(dom.window.location.href = authServer.authorizationEndpoint.withQueryParams(AuthorizeParam(
        webClient.id, webClient.redirectUris.head, webClient.scope, ResponseType.Token,
        Some(state)
      )).toString)
    yield ()

  def handleFetchResourceClick(callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    for
      callbackDataOption <- callbackDataR.get
      _ <- callbackDataOption.fold(IO.unit) { callbackData =>
        for
          respEither <- FetchClientBuilder[IO].create.expect[Json](resourceRequest(callbackData.accessToken)).attempt
          text = respEither.fold(_ => "Error while fetching the protected resource", _.spaces4)
          _ <- textContent(oauthProtectedResourceCls)(text)
        yield ()
      }
    yield ()

  def processCallback(callbackDataR: Ref[IO, Option[OAuthToken]]): IO[Unit] =
    IO(Option(dom.window.location.hash))
      .flatMap(_.filter(_.nonEmpty).flatMap(hash => OAuthToken.from(hash.substring(1)).toOption).fold(IO.unit) {
        oauthToken => IO(Option(dom.window.localStorage.getItem(oauthStateKey))).flatMap { stateOption =>
          stateOption match
            case Some(state) if oauthToken.state.contains(state) =>
              callbackDataR.set(Some(oauthToken)) *>
                textContent(oauthAccessTokenCls)(oauthToken.accessToken) *>
                IO.println(s"access_token: ${oauthToken.accessToken}")
            case _ =>
              IO.println(s"State DOES NOT MATCH: expected ${stateOption.getOrElse("None")} got ${oauthToken.state.getOrElse("None")}") *>
                callbackDataR.set(None) *>
                textContent(oauthProtectedResourceCls)("Error state value did not match")
        }
      })
  end processCallback

  private[this] def onClick[T <: Event](cls: String)(listener: js.Function1[T, _]): IO[Unit] =
    traverseElements(cls)(element => IO(element.addEventListener("click", listener))).void

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
