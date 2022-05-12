package com.peknight.demo.http4s.service

import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.peknight.demo.http4s.emberServer
import org.http4s.ember.server.*
import org.http4s.implicits.*
import org.http4s.server.Router

import scala.concurrent.duration.*

object ServiceApp:
  val services = TweetRoutes.tweetRoutes <+> HelloWorldRoutes.helloWorldRoutes
  val httpApp = Router("/" -> HelloWorldRoutes.helloWorldRoutes, "/api" -> services).orNotFound
  val server = emberServer[IO](httpApp)
  import cats.effect.unsafe.implicits.global
  val shutdown = server.allocated.map(_._2)

