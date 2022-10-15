package com.peknight.demo.frontend.heima.pink.mobile

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.mobile.flowlayout.{Image2XPage, ViewportPage}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*

object MobileApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "viewport" => Ok(ViewportPage.Text.viewportPage)
    case GET -> Root / "image2x" => Ok(Image2XPage.Text.image2xPage)
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/mobile").toRoutes

  // private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
  // }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    htmlRoutes <+> resourceRoutes
    // Router(
    //   "/" -> (htmlRoutes <+> resourceRoutes),
    //   "css" -> cssRoutes
    // )
