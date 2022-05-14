package com.peknight.demo.http4s.dom

import cats.effect.*
import cats.effect.unsafe.implicits.*
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dom.*
import org.scalajs.dom.*

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("FutureClient")
object FetchClientApp:

  case class Activity(activity: String)

  val client = FetchClientBuilder[IO].create

  val activityElement = document.getElementById("activity")

  val fetchActivity: IO[Unit] =
    for
      _ <- IO(activityElement.innerHTML = "<i>fetching ...<i>")
      activity <- client.expect[Activity]("https://www.boredapi.com/api/activity")
      _ <- IO(activityElement.innerHTML = activity.activity)
    yield ()

  val button = document.getElementById("button").asInstanceOf[HTMLButtonElement]

  button.onclick = _ => fetchActivity.unsafeRunAndForget()

