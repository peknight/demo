package com.peknight.demo.fs2.grpc.server.hello

import cats.effect.Sync
import com.peknight.demo.fs2.grpc.protobuf.hello.*
import fs2.Stream
import io.grpc.Metadata

class GreeterFs2GrpcImpl[F[_]: Sync] extends GreeterFs2Grpc[F, Metadata]:

  def sayHello(request: HelloRequest, ctx: Metadata): F[HelloReply] =
    Sync[F].delay(HelloReply(s"Request name is ${request.name}"))

  def sayHelloStream(request: Stream[F, HelloRequest], ctx: Metadata): Stream[F, HelloReply] =
    request.evalMap(req => sayHello(req, ctx))
