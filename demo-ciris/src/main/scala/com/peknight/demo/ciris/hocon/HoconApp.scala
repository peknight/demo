package com.peknight.demo.ciris.hocon

import cats.effect.{IO, IOApp}
import cats.implicits.*
// import com.typesafe.config.{Config, ConfigFactory}
// import lt.dvim.ciris.Hocon.*

import java.time.Period
import scala.concurrent.duration.*

object HoconApp extends IOApp.Simple:
  
  // val config: Config = ConfigFactory.parseString(
  //   """
  //     |rate {
  //     |  elements = 2
  //     |  burst-duration = 100 millis
  //     |  check-interval = 2 weeks
  //     |}
  //   """.stripMargin)

  // val hocon: HoconAt = hoconAt(config)("rate")

  // val hoconConfig =
  //   (
  //     hocon("elements").as[Int],
  //     hocon("burst-duration").as[FiniteDuration],
  //     hocon("check-interval").as[Period]
  //   ).parMapN(Rate.apply)

  val run =
    for
      // conf <- hoconConfig.load[IO]
      // _ <- IO.println(conf)
      _ <- IO.unit
    yield ()
