package com.peknight.demo.fs2.grpc.client

import cats.Monad
import cats.effect.std.Console
import cats.effect.{IO, IOApp, Resource, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.fs2.grpc.protobuf.hello.{GreeterFs2Grpc, HelloRequest}
import fs2.grpc.syntax.all.*
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.{ManagedChannel, Metadata}

object ClientApp extends IOApp.Simple:

  def managedChannelResource[F[_]: Sync]: Resource[F, ManagedChannel] =
    NettyChannelBuilder
      .forAddress("127.0.0.1", 9999)
      .usePlaintext()
      .resource[F]

  def runProgram[F[_]: {Console, Monad}](stub: GreeterFs2Grpc[F, Metadata]): F[Unit] =
    for
      reply <- stub.sayHello(HelloRequest("John Doe"), new Metadata())
      _ <- Console[F].println(reply.message)
    yield ()

  val run = managedChannelResource[IO]
    .flatMap(ch => GreeterFs2Grpc.stubResource[IO](ch))
    .use(runProgram[IO])
