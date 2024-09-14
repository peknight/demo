package com.peknight.demo.js

import cats.Monad
import cats.effect.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import ciris.*
import com.comcast.ip4s.*
import com.peknight.demo.js.page.DemoJsPage
import fs2.Pipe
import fs2.io.file
import fs2.io.net.Network
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.scalatags.*
import org.http4s.server.Server
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.http4s.server.staticcontent.*
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

object DemoJsApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  private val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(DemoJsPage.Text.index)
    case GET -> Root / "tutorial" => Ok(DemoJsPage.Text.tutorial)
    case GET -> Root / "alert" => Ok(DemoJsPage.Text.alertDemo)
    case GET -> Root / "node-append-child" => Ok(DemoJsPage.Text.nodeAppendChildDemo)
    case GET -> Root / "node-on-mouse-move" => Ok(DemoJsPage.Text.nodeOnMouseMoveDemo)
    case GET -> Root / "dom-btoa" => Ok(DemoJsPage.Text.domBtoaDemo)
    case GET -> Root / "dom-local-storage" => Ok(DemoJsPage.Text.domLocalStorageDemo)
    case GET -> Root / "dom-html-canvas-element" => Ok(DemoJsPage.Text.domHtmlCanvasElementDemo)
    case GET -> Root / "dom-fetch" => Ok(DemoJsPage.Text.domFetchDemo)
    case GET -> Root / "dom-websocket" => Ok(DemoJsPage.Text.domWebsocketDemo)
    case GET -> Root / "element-style" => Ok(DemoJsPage.Text.elementStyleDemo)
    case GET -> Root / "hands-on-canvas" => Ok(DemoJsPage.Text.handsOnCanvasTutorialDemo)
    case GET -> Root / "sierpinski-triangle" => Ok(DemoJsPage.Text.sierpinskiTriangleDemo)
    case GET -> Root / "space-invaders" => Ok(DemoJsPage.Text.spaceInvadersDemo)
    case GET -> Root / "dodge-the-dots" => Ok(DemoJsPage.Text.dodgeTheDotsDemo)
    case GET -> Root / "sketchpad" => Ok(DemoJsPage.Text.sketchpadDemo)
    case GET -> Root / "canvas-clock" => Ok(DemoJsPage.Text.canvasClockDemo)
    case GET -> Root / "flappy-box" => Ok(DemoJsPage.Text.flappyBoxDemo)
    case GET -> Root / "canvas-async-advanced" => Ok(DemoJsPage.Text.asyncAdvancedDemo)
    case GET -> Root / "hello-world-0" => Ok(DemoJsPage.Text.helloWorld0Demo)
    case GET -> Root / "hello-world-1" => Ok(DemoJsPage.Text.helloWorld1Demo)
    case GET -> Root / "capital-box" => Ok(DemoJsPage.Text.capitalBoxDemo)
    case GET -> Root / "re-rendering" => Ok(DemoJsPage.Text.reRenderingDemo)
    case GET -> Root / "weather-1" => Ok(DemoJsPage.Text.weather1Demo)
    case GET -> Root / "weather-search" => Ok(DemoJsPage.Text.weatherSearchDemo)
    case GET -> Root / "future-weather" => Ok(DemoJsPage.Text.futureWeatherDemo)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-js/js/target/scala-3.5.0/demo-js-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  private def echoRoutes[F[_]: Monad: Logger](builder: WebSocketBuilder[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val process: Pipe[F, WebSocketFrame, WebSocketFrame] = _.evalMap { frame =>
        info"WebSocket Received: $frame".map(_ => frame)
      }
      builder.build(process)
  }

  private def echoApp[F[_]: Async: Logger](builder: WebSocketBuilder[F]): HttpApp[F] = echoRoutes(builder).orNotFound

  private val storePasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("STORE_PASSWORD").default("123456").secret
  private val keyPasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("KEY_PASSWORD").default("123456").secret

  private def start[F[_]: Async](port: Port)(f: EmberServerBuilder[F] => EmberServerBuilder[F])
  : F[(Server, F[Unit])] =
    for
      storePassword <- storePasswordConfig.load[F]
      keyPassword <- keyPasswordConfig.load[F]
      tlsContext <- Network[F].tlsContext.fromKeyStoreFile(
        file.Path("demo-security/keystore/letsencrypt.keystore").toNioPath,
        storePassword.value.toCharArray, keyPassword.value.toCharArray)
      res <- f(EmberServerBuilder.default[F].withHostOption(None).withPort(port).withTLS(tlsContext)).build.allocated
    yield res

  val run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      _ <- start[IO](port"8080")(_.withHttpApp(
        MiddlewareLogger.httpApp(true, false)(routes.orNotFound)
      ))
      _ <- start[IO](port"10000")(_.withHttpWebSocketApp(echoApp))
      _ <- IO.never
    yield ()
