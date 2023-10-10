package com.peknight.demo.neo4j.parameterized

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.model.query.QueryParam
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
      user = User("Tom", 1980)
      cat = Cat("Waffles")
      hasCat = HasCat(2010)
      _ <- c"CREATE (u: User { $user })".execute.void(driver)
      res <- c"CREATE (u: User { $user })-[r: HAS_CAT { $hasCat }]->(c: Cat { $cat }) RETURN r"
        .query(ResultMapper[HasCat]).single(driver)
      _ <- IO.println(res)
      subQuery1Param = 1
      subQuery1 = c"user.id = $subQuery1Param"
      subQuery2Param = "Tom"
      subQuery2 = c"user.name = $subQuery2Param"
      res <- c"MATCH (user: User) WHERE $subQuery1 OR $subQuery2 RETURN user".query(ResultMapper[User]).list(driver)
      _ <- IO.println(res)
      subSubQueryParam = 1
      subSubQuery = c"user.id = $subSubQueryParam"
      subQuery = c"""$subSubQuery OR user.name = "Luis""""
      hlist = List(QueryParam(1), QueryParam("Luis"), QueryParam(true))
      hmap = Map("foo" -> QueryParam(3), "bar" -> QueryParam("Balmung"), "baz" -> QueryParam(false))
      // Unwind the
      _ <- c"UNWIND $hlist AS x CREATE (: Node {data: x } )".execute.void(driver)
      _ <- c"CREATE (: Node $hmap )".execute.void(driver)
    yield ()
  }
end ParameterizedQueriesApp
