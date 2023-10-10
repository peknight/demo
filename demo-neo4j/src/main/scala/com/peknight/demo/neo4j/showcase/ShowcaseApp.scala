package com.peknight.demo.neo4j.showcase

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.syntax.all.*

object ShowcaseApp extends IOApp.Simple:

  val run: IO[Unit] = driverResource.use { driver =>
    for
      people <- "MATCH (p: Person) RETURN p.name, p.born LIMIT 10".query(ResultMapper.tuple[String, Int])
        .stream(driver).foreach(IO.println).compile.drain
      peopleCC <- "MATCH (p: Person) RETURN id(p) AS id, p.born AS born, p.name AS name, p.notExists AS notExists LIMIT 10"
        .query(ResultMapper[Person]).list(driver)
      _ <- IO.println(peopleCC)
    yield ()
  }
end ShowcaseApp
