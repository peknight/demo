package com.peknight.demo.neo4j.overview

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.generic.implicits.*
import neotypes.mappers.ResultMapper
import neotypes.syntax.all.*

object SessionCreationApp extends IOApp.Simple:
  val run: IO[Unit] = driverResource.use { driver =>
    for
      result <-
        """
          |MATCH (movie: Movie)
          |WHERE toLower(movie.title) CONTAINS "thing"
          |RETURN movie
        """.stripMargin.query(ResultMapper[Movie]).list(driver)
      _ <- IO.println(result)
    yield ()
  }
end SessionCreationApp

