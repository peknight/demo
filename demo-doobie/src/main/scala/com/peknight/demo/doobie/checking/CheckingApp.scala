package com.peknight.demo.doobie.checking

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

object CheckingApp extends IOApp.Simple:

  val y = xa.yolo
  import y.*

  def biggerThan(minPop: Int) =
    sql"""select code, name, population, gnp, indepyear from country where population > $minPop""".query[Country]

  def biggerThan2(minPop: Int) =
    sql"""
      select code, name, population, gnp
      from country
      where population > $minPop
    """.query[Country2]

  val run =
    for
      // Cool
      _ <- biggerThan(0).check
      _ <- IO.println("---")
      _ <- biggerThan2(0).check
      _ <- IO.println("---")
      // 参数元数据不能检查但输出列元数据可以检查的情况可以使用checkOutput
      _ <- biggerThan(0).checkOutput
      _ <- IO.println("---")
    yield ()
