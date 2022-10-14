package com.peknight.demo.frontend.heima.pink.css3

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.css3.animation.{AnimationPage, AnimationStyles}
import com.peknight.demo.frontend.heima.pink.css3.transform2d.Transform2DPage
import com.peknight.demo.frontend.heima.pink.css3.transform3d.{Transform3DPage, Transform3DStyles}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*

object Css3App extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "transform2d" => Ok(Transform2DPage.Text.transform2D)
    case GET -> Root / "animation" => Ok(AnimationPage.Text.animation)
    case GET -> Root / "transform3d" => Ok(Transform3DPage.Text.transform3D)
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/css3").toRoutes

  private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "animation.css" => Ok(AnimationStyles.render[String])
    case GET -> Root / "transform3d.css" => Ok(Transform3DStyles.render[String])
  }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes
    )
