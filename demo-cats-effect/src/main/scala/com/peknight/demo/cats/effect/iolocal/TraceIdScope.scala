package com.peknight.demo.cats.effect.iolocal

import cats.effect.{IO, IOLocal, Resource}
import cats.syntax.functor.*

trait TraceIdScope[F[_]]:
  def get: F[TraceId]
  def scope(traceId: TraceId): Resource[F, Unit]

object TraceIdScope:
  def apply[F[_]](using ev: TraceIdScope[F]): TraceIdScope[F] = ev
  def fromIOLocal: IO[TraceIdScope[IO]] =
    for
      local <- IOLocal(TraceId("global"))
    yield
      new TraceIdScope[IO]:
        def get: IO[TraceId] = local.get
        def scope(traceId: TraceId): Resource[IO, Unit] =
          Resource.make(local.getAndSet(traceId))(previous => local.set(previous)).void
  end fromIOLocal

end TraceIdScope
