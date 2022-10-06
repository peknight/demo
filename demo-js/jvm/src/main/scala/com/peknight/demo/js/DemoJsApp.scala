package com.peknight.demo.js

import cats.effect.*
import com.comcast.ip4s.*
import com.peknight.demo.js.page.DemoJsPage
import fs2.io.file
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.scalatags.*
import org.http4s.server.Server
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent.*

object DemoJsApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(DemoJsPage.Text.index)
    case GET -> Root / "tutorial" => Ok(DemoJsPage.Text.tutorial)
    case GET -> Root / "alert" => Ok(DemoJsPage.Text.alertDemo)
    case GET -> Root / "node_append_child" => Ok(DemoJsPage.Text.nodeAppendChildDemo)
    case GET -> Root / "node_on_mouse_move" => Ok(DemoJsPage.Text.nodeOnMouseMoveDemo)
    case GET -> Root / "dom_btoa" => Ok(DemoJsPage.Text.domBtoaDemo)
    case GET -> Root / "dom_local_storage" => Ok(DemoJsPage.Text.domLocalStorageDemo)
    case GET -> Root / "dom_html_canvas_element" => Ok(DemoJsPage.Text.domHtmlCanvasElementDemo)
    case GET -> Root / "dom_fetch" => Ok(DemoJsPage.Text.domFetchDemo)
    case GET -> Root / "dom_websocket" => Ok(DemoJsPage.Text.domWebsocketDemo)
    case GET -> Root / "element_style" => Ok(DemoJsPage.Text.elementStyleDemo)
    case GET -> Root / "hands_on_canvas" => Ok(DemoJsPage.Text.handsOnCanvasTutorialDemo)
    case GET -> Root / "sierpinski_triangle" => Ok(DemoJsPage.Text.sierpinskiTriangleDemo)
    case GET -> Root / "space_invaders" => Ok(DemoJsPage.Text.spaceInvadersDemo)
    case GET -> Root / "dodge_the_dots" => Ok(DemoJsPage.Text.dodgeTheDotsDemo)
    case GET -> Root / "sketchpad" => Ok(DemoJsPage.Text.sketchpadDemo)
    case GET -> Root / "canvas_clock" => Ok(DemoJsPage.Text.canvasClockDemo)
    case GET -> Root / "flappy_box" => Ok(DemoJsPage.Text.flappyBoxDemo)
    case GET -> Root / "canvas_async_advanced" => Ok(DemoJsPage.Text.asyncAdvancedDemo)
    case GET -> Root / "hello_world_0" => Ok(DemoJsPage.Text.helloWorld0Demo)
    case GET -> Root / "hello_world_1" => Ok(DemoJsPage.Text.helloWorld1Demo)
    case GET -> Root / "capital_box" => Ok(DemoJsPage.Text.capitalBoxDemo)
    case GET -> Root / "re_rendering" => Ok(DemoJsPage.Text.reRenderingDemo)
    case GET -> Root / "weather_1" => Ok(DemoJsPage.Text.weather1Demo)
    case GET -> Root / "weather_search" => Ok(DemoJsPage.Text.weatherSearchDemo)
    case GET -> Root / "future_weather" => Ok(DemoJsPage.Text.futureWeatherDemo)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-js/js/target/scala-3.2.0/demo-js-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  def start[F[_]: Async](httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      .withHostOption(None)
      .withPort(port"8080")
      .withHttpApp(Logger.httpApp(true, true)(httpApp))
      .build.allocated

  val run: IO[Unit] =
    for
      _ <- start[IO](routes.orNotFound)
      _ <- IO.never
    yield ()
