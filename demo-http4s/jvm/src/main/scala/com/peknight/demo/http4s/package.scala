package com.peknight.demo

import cats.effect.{Async, Resource}
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

package object http4s:

  def runEmberServer[F[_]: Async](httpApp: HttpApp[F]): F[Unit] =
    emberServer[F](httpApp).use(_ => Async[F].never)

  def emberServer[F[_]: Async](httpApp: HttpApp[F]): Resource[F, Server] =
    EmberServerBuilder.default[F]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build

