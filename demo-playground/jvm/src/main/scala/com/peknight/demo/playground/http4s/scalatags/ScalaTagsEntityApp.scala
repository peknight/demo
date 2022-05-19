package com.peknight.demo.playground.http4s.scalatags

import cats.effect.*
import com.comcast.ip4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.scalatags.*
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent.*
import org.http4s.HttpRoutes
import org.http4s.Method
import scalatags.Text.all.*
import scalacss.ProdDefaults.*

object ScalaTagsEntityApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(html(head(style(CustomStyle.render)), body(h1("ScalaTagsEntityDemo"))))
  }

  val run =
    EmberServerBuilder.default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(Logger.httpApp(true, true)(routes.orNotFound))
      .build
      .use(_ => IO.never)


