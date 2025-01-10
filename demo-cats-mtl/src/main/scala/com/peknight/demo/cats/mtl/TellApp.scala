package com.peknight.demo.cats.mtl

import cats.Monad
import cats.data.{Chain, Writer}
import cats.mtl.Tell
import cats.syntax.all.*

object TellApp extends App:
  def serviceCall[F[_]: Monad](params: ServiceParams): F[ServiceResult] =
    // A fake call to some external service, replace with real implementation
    ServiceResult(0, List("Raven Enterprises")).pure[F]

  def serviceCallWithLog[F[_]: Monad](params: ServiceParams)(using F: Tell[F, Chain[String]]): F[ServiceResult] =
    for
      _ <- F.tell(Chain.one(show"Call to service with ${params.option1} and ${params.option2}"))
      result <- serviceCall[F](params)
      _ <- F.tell(Chain.one(show"Service returned: userId: ${result.id}; companies: ${result.companies}"))
    yield
      result

  val (log, result): (Chain[String], ServiceResult) =
    serviceCallWithLog[[X] =>> Writer[Chain[String], X]](ServiceParams("business", 42)).run

  println(log)
  println(result)
end TellApp
