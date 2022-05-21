package com.peknight.demo.ciris.modules

import cats.effect.{IO, IOApp}
import ciris.circe.yaml.*
import ciris.{ConfigDecoder, env}
import io.circe.generic.auto.*
import io.circe.{Decoder, Json}

object CirceYamlApp extends IOApp.Simple:

  val jsonConfigDecoder: ConfigDecoder[String, Json] = ConfigDecoder[String, Json]

  given ConfigDecoder[String, SerialNumber] = circeYamlConfigDecoder[SerialNumber]("SerialNumber")

  val run =
    for
      _ <- env("CIRCE_YAML_SERIAL_NUMBER").as[SerialNumber].attempt[IO].flatMap(IO.println)
    yield ()
