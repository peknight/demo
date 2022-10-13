package com.peknight.demo.frontend.heima.pink.css3

import cats.effect.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.css3.transform2d.Transform2DPage
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.typelevel.log4cats.Logger

object Css3App extends DemoFrontEndHttp4sApp:

  def routes(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "transform2d" => Ok(Transform2DPage.Text.transform2D)
  }

