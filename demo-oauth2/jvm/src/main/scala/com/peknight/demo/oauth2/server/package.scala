package com.peknight.demo.oauth2

import cats.effect.{Async, Resource}
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.middleware.Logger

package object server:

  val host = host"localhost"

  def start[F[_]: Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      .withHost(host)
      .withPort(port)
      .withHttpApp(Logger.httpApp(true, true)(httpApp))
      .build.allocated