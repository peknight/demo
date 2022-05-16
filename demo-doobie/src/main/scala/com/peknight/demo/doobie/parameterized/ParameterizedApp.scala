package com.peknight.demo.doobie.parameterized

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.selecting.Country
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import fs2.Stream

object ParameterizedApp extends IOApp.Simple:

  val y = xa.yolo
  import y._

  def biggerThan(minPop: Int) =
    sql"""select code, name, population, gnp from country where population > $minPop""".query[Country]

  def populationIn(range: Range) =
    sql"""
      select code, name, population, gnp
      from country
      where population > ${range.min}
      and   population < ${range.max}
    """.query[Country]

  def populationIn(range: Range, codes: NonEmptyList[String]) =
    val q = fr"""
      select code, name, population, gnp
      from country
      where population > ${range.min}
      and   population < ${range.max}
      and   """ ++ Fragments.in(fr"code", codes) // code IN (...)
    q.query[Country]

  val q = """
    select code, name, population, gnp
    from country
    where population > ?
    and   population < ?
  """

  def proc(range: Range): Stream[ConnectionIO, Country] =
    HC.stream[Country](q, HPS.set((range.min, range.max)), 512)

  // Set parameters as (String, Boolean) starting at index 1 (default)
  HPS.set(("foo", true))

  // Set parameters as (String, Boolean) starting at index 1 (explicit)
  HPS.set(1, ("foo", true))

  // Set parameters individually
  HPS.set(1, "foo") *> HPS.set(2, true)

  // Or out of order, who cares?
  HPS.set(2, true) *> HPS.set(1, "foo")

  // low level 方法要指定类型
  FPS.setString(1, "foo") *> FPS.setBoolean(2, true)

  val run =
    for
      _ <- biggerThan(150000000).quick
      _ <- IO.println("---")
      _ <- populationIn(150000000 to 200000000).quick
      _ <- IO.println("---")
      _ <- populationIn(100000000 to 300000000, NonEmptyList.of("USA", "BRA", "PAK", "GBR")).quick
      _ <- IO.println("---")
      _ <- proc(150000000 to 200000000).quick
      _ <- IO.println("---")
      _ <- IO.println(Write[(String, Boolean)])
      _ <- IO.println(Write[Country])
    yield ()

