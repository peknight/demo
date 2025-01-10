package com.peknight.demo.cats.mtl

import cats.Monad
import cats.data.State
import cats.mtl.Stateful
import cats.syntax.all.*

object StatefulApp extends App:
  def serviceCall[F[_]: Monad](id: String): F[ServiceResult] =
    // a fake call to some external service, impure, so don't do this at home!
    println(show"Called service with $id")
    ServiceResult(0, List("Raven Enterprises")).pure[F]

  type Cache = Map[String, ServiceResult]

  def cachedServiceCall[F[_]: Monad](id: String)(using F: Stateful[F, Cache]): F[ServiceResult] =
    for
      cache <- F.get
      result <- cache.get(id) match
        case Some(result) => result.pure[F]
        case None => serviceCall[F](id)
    yield
      result

  def serviceCallAndWriteToCache[F[_]: Monad](id: String)(using F: Stateful[F, Cache]): F[ServiceResult] =
    for
      result <- serviceCall[F](id)
      cache <- F.get
      _ <- F.set(cache.updated(id, result))
    yield
      result

  def cachedServiceCallAndWriteToCache[F[_]: Monad](id: String)(using F: Stateful[F, Cache]): F[ServiceResult] =
    for
      cache <- F.get
      result <- cache.get(id) match
        case Some(result) => result.pure[F]
        case None => serviceCallAndWriteToCache[F](id)
    yield
      result

  def invalidate[F[_]](using F: Stateful[F, Cache]): F[Unit] = F.set(Map.empty)

  def program[F[_]: Monad](using F: Stateful[F, Cache]): F[ServiceResult] =
    for
      result1 <- cachedServiceCallAndWriteToCache[F]("ab94d2")
      // This should use the cached value
      result2 <- cachedServiceCallAndWriteToCache[F]("ab94d2")
      _ <- invalidate[F]
      // This should access the service again
      freshResult <- cachedServiceCallAndWriteToCache[F]("ab94d2")
    yield
      freshResult

  def initialCache: Cache = Map.empty
  val (result, cache) = program[[X] =>> State[Cache, X]].run(initialCache).value
end StatefulApp
