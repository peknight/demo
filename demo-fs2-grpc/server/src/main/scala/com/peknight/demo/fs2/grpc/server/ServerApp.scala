package com.peknight.demo.fs2.grpc.server

import cats.effect.{Async, IO, IOApp, Resource}
import com.peknight.demo.fs2.grpc.protobuf.hello.GreeterFs2Grpc
import com.peknight.demo.fs2.grpc.server.hello.GreeterFs2GrpcImpl
import fs2.grpc.syntax.all.*
import io.grpc.ServerServiceDefinition
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder

object ServerApp extends IOApp.Simple:

  def helloService[F[_]: Async]: Resource[F, ServerServiceDefinition] =
    GreeterFs2Grpc.bindServiceResource[F](new GreeterFs2GrpcImpl[F])

  val run = helloService[IO].use { service =>
    NettyServerBuilder.forPort(9999)
      .addService(service)
      .resource[IO]
      .evalMap(server => IO(server.start()))
      .useForever
  }

