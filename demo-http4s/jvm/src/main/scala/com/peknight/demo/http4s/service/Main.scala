package com.peknight.demo.http4s.service

import cats.effect.*
import com.comcast.ip4s.*
import com.peknight.demo.http4s.runEmberServer
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*

object Main extends IOApp.Simple:

  val helloWorldService = HelloWorldRoutes.helloWorldRoutes.orNotFound

  def run = runEmberServer[IO](helloWorldService)

