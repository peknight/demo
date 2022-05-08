package com.peknight.demo.http4s.service

import cats.effect.*
import com.comcast.ip4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*

object Main extends IOApp.Simple:

  val helloWorldService = HelloWorldRoutes.helloWorldRoutes.orNotFound

  //noinspection DuplicatedCode
  def run = EmberServerBuilder.default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(helloWorldService)
    .build
    .use(_ => IO.never)

