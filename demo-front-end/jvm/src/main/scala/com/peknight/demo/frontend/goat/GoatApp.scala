package com.peknight.demo.frontend.goat

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.goat.login.LoginPage
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*

object GoatApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(LoginPage.Text.login)
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/goat/login/").toRoutes

  def routes(using Logger[IO]): HttpRoutes[IO] = htmlRoutes <+> resourceRoutes