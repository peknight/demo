package com.peknight.demo.http4s.dom

import _root_.scalatags.Text.all.{title as _, *}
import _root_.scalatags.Text.tags2.title
import cats.effect.*
import com.peknight.demo.http4s.*
import fs2.io.file
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*

object DomApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(page)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-http4s/js/target/scala-3.6.2/demo-http4s-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  val page: Frag =
    html(
      head(
        meta(httpEquiv := "Content-Type", content := "text/html; charset=UTF-8"),
        title("Hands-on Scala.js Webpage Demo")
      ),
      body(
        div(cls := "fetch_client_demo")(
          h3("I'm bored."),
          button(id := "button", `type` := "button")("Fetch Activity"),
          div(id := "activity")
        ),
        div(cls := "websocket_client_demo")(
          h3("Send Text Frame"),
          input(`type` := "text", id := "ws_message"),
          button(`type` := "button", id := "ws_button")("Send"),
          h4("Sent"),
          div(id := "ws_sent"),
          h4("Received"),
          div(id := "ws_received"),
          script(`type` := "text/javascript", src := "/main.js")
        )
      )
    )

  val run: IO[Unit] = runLogEmberServer[IO](routes.orNotFound)
