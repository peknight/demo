package com.peknight.demo.cats.monadtransformer

import cats.FlatMap
import cats.data.Kleisli

object KleisliApp extends App {
  val twice: Int => Int =
    x => x * 2

  val countCats: Int => String =
    x => if (x == 1) "1 cat" else s"$x cats"

  val twiceAsManyCats: Int => String = twice andThen countCats

  println(twiceAsManyCats(1))

  val parse: Kleisli[Option, String, Int] =
    Kleisli((s: String) => if (s.matches("-?[0-9]+")) Some(s.toInt) else None)

  val reciprocal:Kleisli[Option, Int, Double] = Kleisli(i => if (i != 0) Some(1.0 / i) else None)

  val parseAndReciprocal: Kleisli[Option, String, Double] = reciprocal.compose(parse)

  case class DbConfig(url: String, user: String, pass: String)

  trait Db
  object Db {
    val fromDbConfig: Kleisli[Option, DbConfig, Db] = ???
  }

  case class ServiceConfig(addr: String, port: Int)

  trait Service

  object Service {
    val fromServiceConfig: Kleisli[Option, ServiceConfig, Service] = ???
  }

  case class AppConfig(dbConfig: DbConfig, serviceConfig: ServiceConfig)

  class App(db: Db, service: Service)

  def appFromAppConfig: Kleisli[Option, AppConfig, App] = for {
    db <- Db.fromDbConfig.local[AppConfig](_.dbConfig)
    sv <- Service.fromServiceConfig.local[AppConfig](_.serviceConfig)
  } yield new App(db, sv)
}
