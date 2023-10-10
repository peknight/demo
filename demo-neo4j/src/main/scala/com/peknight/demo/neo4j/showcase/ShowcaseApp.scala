package com.peknight.demo.neo4j.showcase

import cats.effect.{IO, IOApp, Resource}
import fs2.Stream
import neotypes.cats.effect.implicits.IOAsync
import neotypes.fs2.Fs2IoStream
import neotypes.fs2.implicits.Fs2IoStream
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.syntax.all.*
import neotypes.{GraphDatabase, StreamDriver}
import org.neo4j.driver.AuthTokens

object ShowcaseApp extends IOApp.Simple:
  val driverResource: Resource[IO, StreamDriver[Fs2IoStream, IO]] =
    GraphDatabase.streamDriver[Fs2IoStream](
      "bolt://localhost:7687",
      AuthTokens.basic("neo4j", "neotypes")
    )

  val run: IO[Unit] = driverResource.use { driver =>
    for
      people <- "MATCH (p: Person) RETURN p.name, p.born LIMIT 10".query(ResultMapper.tuple[String, Int])
        .stream(driver).foreach(IO.println).compile.drain
      peopleCC <- "MATCH (p: Person) RETURN p LIMIT 10".query(ResultMapper[Person])
        .stream(driver).foreach(IO.println).compile.drain
    yield ()
  }
end ShowcaseApp
