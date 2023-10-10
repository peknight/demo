package com.peknight.demo

import cats.effect.{IO, Resource}
import neotypes.cats.effect.implicits.IOAsync
import neotypes.fs2.Fs2IoStream
import neotypes.fs2.implicits.Fs2IoStream
import neotypes.{GraphDatabase, StreamDriver}
import org.neo4j.driver.AuthTokens

package object neo4j:
  val driverResource: Resource[IO, StreamDriver[Fs2IoStream, IO]] =
    GraphDatabase.streamDriver[Fs2IoStream](
      "bolt://localhost:7687",
      AuthTokens.basic("neo4j", "neotypes")
    )
end neo4j
