package com.peknight.demo.neo4j.parameterized

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.syntax.all.*

object ParameterizedQueriesApp extends IOApp.Simple:
  val run: IO[Unit] = driverResource.use { driver =>
    for
      _ <- IO.unit
      name = "John"
      born = 1980
      _ <- c"CREATE (a: Test { name: $name, born: $born })".execute.void(driver)
      label = "User"
      _ <- (c"CREATE (a: " + label + c" { name: $name })").execute.void(driver)
      _ <- c"CREATE (a: #$label { name: $name })".execute.void(driver)
      _ <-
        c"""CREATE (a: #$label {
           name: $name,
           born: $born
         })""".execute.void(driver)
      user = User("John", 1980)
      cat = Cat("Waffles")
      hasCat = HasCat(2010)
      _ <- c"CREATE (u: User { $user })".execute.void(driver)
      res <- c"CREATE (u: User { $user })-[r: HAS_CAT { $hasCat }]->(c: Cat { $cat }) RETURN r"
        .query(ResultMapper[HasCat]).single(driver)
      _ <- IO.println(res)
    yield ()
  }
end ParameterizedQueriesApp
