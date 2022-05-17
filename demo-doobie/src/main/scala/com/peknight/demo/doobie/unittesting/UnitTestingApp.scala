package com.peknight.demo.doobie.unittesting

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.checking.Country
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

object UnitTestingApp extends IOApp.Simple:

  val trivial = sql"""select 42, 'foo'::varchar""".query[(Int, String)]

  def biggerThan(minPop: Short) =
    sql"""select code, name, population, gnp, indepyear from country where population > $minPop""".query[Country]

  def update(oldName: String, newName: String) =
    sql"""update country set name = $newName where name = $oldName""".update

  val run =
    for
      _ <- IO.unit
    yield ()

