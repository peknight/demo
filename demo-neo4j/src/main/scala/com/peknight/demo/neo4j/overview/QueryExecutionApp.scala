package com.peknight.demo.neo4j.overview

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.model.query.QueryParam
import neotypes.syntax.all.*

import scala.collection.immutable.{ListMap, ListSet}

object QueryExecutionApp extends IOApp.Simple:
  val run: IO[Unit] = driverResource.use { driver =>
    for
      res <- "CREATE (p: Person { name: 'John1', born: 1980 })".execute.resultSummary(driver)
      _ <- IO.println(res)
      _ <- "CREATE (p: Person { name: $name, born: $born })".execute
        .withParams(Map("name" -> QueryParam("John2"), "born" -> QueryParam(1980))).void(driver)
      name = "John3"
      born = 1980
      _ <- c"CREATE (p: Person { name: $name, born: $born })".execute.void(driver)
      _ <- "CREATE (p: Person { name: 'Charlize Theron', born: 1975 })".execute.void(driver)
      // Single.
      res <- "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p.name".query(ResultMapper.string).single(driver)
      _ <- IO.println(res)
      res <- "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p".query(ResultMapper[Person]).single(driver)
      _ <- IO.println(res)
      res <- "MATCH (p: Person { name: 'Charlize Theron' }) RETURN p"
        .query(ResultMapper.neoMap(ResultMapper.identity)).single(driver)
      _ <- IO.println(res)
      res <- "MATCH (p: Person { name: '1234' }) RETURN p.born".query(ResultMapper.int).single(driver).option
      _ <- IO.println(res)
      // List.
      res <- "MATCH (p: Person { name: 'Charlize Theron' })-[]->(m: Movie) RETURN p,m"
        .query(ResultMapper.tuple[Person, Movie]).list(driver)
      _ <- IO.println(res)
      // Map.
      res <- "MATCH (p: Person { name: 'Charlize Theron' })-[]->(m: Movie) RETURN p,m"
        .query(ResultMapper.tuple[Person, Movie]).map(driver)
      _ <- IO.println(res)
      // Any collection.
      res <- "MATCH (p: Person { name: 'Charlize Theron' })-[]->(m: Movie) RETURN p,m"
        .query(ResultMapper.tuple[Person, Movie]).collectAs(ListSet, driver)
      _ <- IO.println(res)
      res <- "MATCH (p: Person { name: 'Charlize Theron' })-[]->(m: Movie) RETURN p,m"
        .query(ResultMapper.tuple[Person, Movie]).collectAs(ListMap, driver)
      _ <- IO.println(res)
    yield ()
  }
end QueryExecutionApp

