package com.peknight.demo.frontend.heima.pink.pinyougou.app

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.pinyougou.page.*
import com.peknight.demo.frontend.heima.pink.pinyougou.style.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.*
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*

object PinyougouApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(IndexPage.Text.index)
    case GET -> Root / "index.html" => Ok(IndexPage.Text.index)
    case GET -> Root / "list.html" => Ok(ListPage.Text.list)
    case GET -> Root / "register.html" => Ok(RegisterPage.Text.register)
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/pinyougou").toRoutes

  private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "base.css" => Ok(BaseStyles.render[String])
    case GET -> Root / "fonts.css" => Ok(FontsStyles.render[String])
    case GET -> Root / "common.css" => Ok(CommonStyles.render[String])
    case GET -> Root / "index.css" => Ok(IndexStyles.render[String])
    case GET -> Root / "list.css" => Ok(ListStyles.render[String])
    case GET -> Root / "register.css" => Ok(RegisterStyles.render[String])
  }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes
    )