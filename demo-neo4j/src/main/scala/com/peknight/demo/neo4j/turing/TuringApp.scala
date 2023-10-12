package com.peknight.demo.neo4j.turing

import cats.effect.{IO, IOApp}
import com.peknight.demo.neo4j.driverResource
import neotypes.mappers.ResultMapper
import neotypes.syntax.all.*

object TuringApp extends IOApp.Simple:
  val run: IO[Unit] = driverResource.use { driver =>
    for
      _ <- """MATCH (n) DETACH DELETE n""".execute.void(driver)
      _ <-
        """
          |LOAD CSV WITH HEADERS FROM 'file:///triples.csv' AS row
          |CREATE (:RelationNode { head: row.head, tail: row.tail, label: row.label})
        """.stripMargin.execute.void(driver)
      _ <-
        """
          |LOAD CSV FROM 'file:///characters.csv' AS row
          |CREATE (:Person { name: row[0] })
        """.stripMargin.execute.void(driver)
      _ <-
        """
          |MATCH (p1: Person),(r: RelationNode),(p2: Person)
          |WHERE p1.name = r.head AND p2.name = r.tail
          |CALL apoc.create.relationship(p2, r.label, {relation: r.label}, p1) YIELD rel
          |RETURN p2, rel, p1
        """.stripMargin.query(ResultMapper.identity).stream(driver).foreach(IO.println).compile.drain
      _ <- """MATCH (r: RelationNode) DELETE r""".execute.void(driver)
    yield ()
  }
end TuringApp
