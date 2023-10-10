package com.peknight.demo.neo4j.transactions

import cats.Monad
import cats.effect.{IO, IOApp}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.neo4j.driverResource
import neotypes.mappers.ResultMapper
import neotypes.model.query.QueryParam
import neotypes.syntax.all.*
import neotypes.{AsyncDriver, AsyncTransaction, TransactionConfig}

import scala.concurrent.duration.*
object TransactionsApp extends IOApp.Simple:
  def result[F[_]](driver: AsyncDriver[F]): F[String] =
    "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p.name"
      .query(ResultMapper.string)
      .single(driver)

  def multipleResult[F[_]: Monad](driver: AsyncDriver[F]): F[(String, String)] = driver.transact { tx =>
    for
      r1 <- "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p.name".query(ResultMapper.string).single(tx)
      r2 <- "MATCH (p: Person { name: 'Tom Hanks' }) RETURN p.name".query(ResultMapper.string).single(tx)
    yield (r1, r2)
  }

  def rollbackResult[F[_] : Monad](driver: AsyncDriver[F]): F[Unit] = driver.transact { tx =>
    for
      _ <- "CREATE (p: Person { name: 'Charlize Theron Test' })".execute.void(tx)
      _ <- "CREATE (p: Person { name: 'Tom Hanks Test' })".execute.void(tx)
      _ <- tx.rollback
    yield ()
  }

  val config =
    TransactionConfig
      .default
      .withTimeout(2.seconds)
      .withMetadata(Map("foo" -> QueryParam("bar"), "baz" -> QueryParam(10)))

  def customTransaction[F[_]](driver: AsyncDriver[F]): F[AsyncTransaction[F]] =
    driver.transaction(config)

  def result1[F[_]: Monad](driver: AsyncDriver[F]): F[(String, String)] = driver.transact(config) { tx =>
    for
      r1 <- "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p.name".query(ResultMapper.string).single(tx)
      r2 <- "MATCH (p: Person { name: 'Tom Hanks' }) RETURN p.name".query(ResultMapper.string).single(tx)
    yield (r1, r2)
  }

  def result2[F[_]](driver: AsyncDriver[F]): F[String] =
    "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p.name"
      .query(ResultMapper.string)
      .single(driver, config)

  val run: IO[Unit] = driverResource.use { driver =>
    for
      res <- result(driver)
      _ <- IO.println(res)
      res <- multipleResult(driver)
      _ <- IO.println(res)
      _ <- rollbackResult(driver).attempt.flatMap(IO.println)
      res <- result1(driver)
      _ <- IO.println(res)
      res <- result2(driver)
      _ <- IO.println(res)
    yield ()
  }
end TransactionsApp
