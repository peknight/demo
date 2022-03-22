package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.monads.Monad
import com.peknight.demo.fpinscala.monads.Monad.optionMonad
import com.peknight.demo.fpinscala.parsing.Sliceable
import com.peknight.demo.fpinscala.parsing.Sliceable.{string => _, _}
import com.peknight.demo.fpinscala.parsing.SliceableType.Parser

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Date
import scala.language.implicitConversions

object ApplicativeApp extends App {
  val depts: Map[String, String] = Map("Alice" -> "Tech")
  val salaries: Map[String, Double] = Map("Alice" -> 100.0)
  val aliceDeptSalary: Option[String] = optionMonad.map2(depts.get("Alice"), salaries.get("Alice"))((dept, salary) =>
    s"Alice in $dept makes $salary per year"
  )

  val idsByName: Map[String, Int] = Map("Bob" -> 123)
  val deptsById: Map[Int, String] = Map(123 -> "HR")
  val salariesById: Map[Int, Double] = Map(123 -> 50.0)
  val bobDeptSalary: Option[String] = idsByName.get("Bob").flatMap(id =>
    optionMonad.map2(deptsById.get(id), salariesById.get(id))((dept, salary) => s"Bob in $dept makes $salary per year")
  )

  val parserMonad: Monad[Parser] = Monad.parserMonad(Sliceable)

  implicit def tok(s: String) = token(Sliceable.string(s))

  val dateParser: Parser[Date] = "[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{1,4}".r.map { dateStr => Date.from(
    LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy"))
      .atStartOfDay(ZoneId.systemDefault()).toInstant
  )}
  val tempParser: Parser[Double] = double
  val rowParser: Parser[Row] = parserMonad.map2(dateParser <* ",", tempParser)(Row)
  val rowsParser: Parser[List[Row]] = root(whitespace *> rowParser.sep(whitespace))

  val rowsFile =
    """
      |1/1/2010, 25
      |2/1/2010, 28
      |3/1/2010, 42
      |4/1/2010, 53
      |""".stripMargin

  Sliceable.run(rowsParser)(rowsFile).fold(println, println)

  val tempHeader: Parser[Parser[Double]] = Sliceable.string("Temperature").map(_ => tempParser)
  val dateHeader: Parser[Parser[Date]] = Sliceable.string("Date").map(_ => dateParser)

  def headerParser[A, B, C](header1: Parser[Parser[A]], header2: Parser[Parser[B]])(f: (A, B) => C): Parser[Parser[C]] =
    parserMonad.map2(header1 <* ",", header2) { (header1Parser, header2Parser) =>
      parserMonad.map2(header1Parser <* ",", header2Parser)(f)
    }

  val header: Parser[Parser[Row]] = token("#" *> (headerParser(tempHeader, dateHeader)((temp, date) => Row(date, temp)) or
    headerParser(dateHeader, tempHeader)(Row)))

  val rowsParser2: Parser[List[Row]] = root(whitespace *> parserMonad.flatMap(header)(_.sep(whitespace)))

  val rowsFile2 =
    """
      |# Temperature, Date
      |25, 1/1/2010
      |28, 2/1/2010
      |42, 3/1/2010
      |53, 4/1/2010
      |""".stripMargin

  Sliceable.run(rowsParser2)(rowsFile2.trim).fold(println, println)




}
