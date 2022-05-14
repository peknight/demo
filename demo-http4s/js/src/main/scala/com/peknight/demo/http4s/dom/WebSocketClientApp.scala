package com.peknight.demo.http4s.dom

import cats.effect.*
import cats.effect.unsafe.implicits.*
import org.http4s.client.websocket.*
import org.http4s.dom.*
import org.http4s.syntax.all.*
import org.scalajs.dom.*

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("WebSocketClient")
object WebSocketClientApp:

  val message = document.getElementById("ws_message").asInstanceOf[HTMLInputElement]
  val button = document.getElementById("ws_button").asInstanceOf[HTMLButtonElement]
  val sent = document.getElementById("ws_sent").asInstanceOf[HTMLElement]
  val received = document.getElementById("ws_received").asInstanceOf[HTMLElement]

  val request = WSRequest(uri"wss://ws.postman-echo.com/raw")

  val app = WebSocketClient[IO].connectHighLevel(request).use { conn =>
    def log(e: HTMLElement, text: String): IO[Unit] = IO {
      val p = document.createElement("p")
      p.innerHTML = text
      e.appendChild(p)
      ()
    }

    val sendMessage: IO[Unit] =
      for
        text <- IO(message.value)
        frame = WSFrame.Text(text)
        _ <- conn.send(frame)
        _ <- log(sent, frame.toString)
      yield ()

    val receiveMessages: IO[Unit] =
      conn.receiveStream.evalTap(frame => log(received, frame.toString)).compile.drain

    val logCloseFrame: IO[Unit] = conn.closeFrame.get.flatMap(frame => log(received, frame.toString))

    val registerOnClick = IO(button.onclick = _ => sendMessage.unsafeRunAndForget())
    val deregisterOnClick = IO(button.onclick = null)

    registerOnClick *> receiveMessages *> logCloseFrame *> deregisterOnClick
  }

  app.unsafeRunAndForget()

