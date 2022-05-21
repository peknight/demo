package com.peknight.demo.ciris.modules

import cats.effect.{IO, IOApp}
import ciris.circe.*
import ciris.{ConfigDecoder, env}
import io.circe.generic.auto.*
import io.circe.{Decoder, Json}

object CirceApp extends IOApp.Simple:

  val jsonConfigDecoder: ConfigDecoder[String, Json] = ConfigDecoder[String, Json]

  given ConfigDecoder[String, SerialNumber] = circeConfigDecoder[SerialNumber]("SerialNumber")

  val run =
    for
      _ <- env("CIRCE_SERIAL_NUMBER").as[SerialNumber].attempt[IO].flatMap(IO.println)
    yield ()

