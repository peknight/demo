package com.peknight.demo

import cats.Functor
import cats.data.OptionT
import cats.effect.{Async, Resource}
import cats.syntax.functor.*
import cats.syntax.option.*
import com.comcast.ip4s.*
import fs2.io.file.Path
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.middleware.Logger

package object oauth2:

  val serverHost = host"localhost"

  def start[F[_]: Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      .withHost(serverHost)
      .withPort(port)
      .withHttpApp(Logger.httpApp(true, true)(httpApp))
      .build.allocated

  val databaseNoSqlPath =
    Path("/Users/pek/project/github/oauthinaction/oauth-in-action-code/exercises/ch-3-ex-2/database.nosql")
    
  extension [F[_]: Functor, A](fa: F[A])
    def optionT = OptionT(fa.map(_.some))


