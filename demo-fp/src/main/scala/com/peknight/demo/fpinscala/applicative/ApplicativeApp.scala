package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.applicative.Validation.{Failure, Success, validationApplicative}
import com.peknight.demo.fpinscala.monads.Monad
import com.peknight.demo.fpinscala.monads.Monad.{lazyListMonad, optionMonad}
import com.peknight.demo.fpinscala.parsing.Sliceable
import com.peknight.demo.fpinscala.parsing.Sliceable.{string as _, *, given}
import com.peknight.demo.fpinscala.parsing.SliceableType.Parser

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Date
import scala.language.implicitConversions

object ApplicativeApp extends App:

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

  given tok: Conversion[String, Parser[String]] = (a: String) => token(Sliceable.string(a))

  def parseDate(dateStr: String, pattern: String): Date = Date.from(
    LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern)).atStartOfDay(ZoneId.systemDefault()).toInstant
  )

  val dateParser: Parser[Date] = "[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{1,4}".r.map(parseDate(_, "M/d/yyyy"))
  val tempParser: Parser[Double] = double
  val rowParser: Parser[Row] = parserMonad.map2(dateParser <* ",", tempParser)(Row.apply)
  val rowsParser: Parser[List[Row]] = root(whitespace *> many(rowParser <* whitespace))

  val rowsFile =
    """
      |1/1/2010, 25
      |2/1/2010, 28
      |3/1/2010, 42
      |4/1/2010, 53
    """.trim.stripMargin

  Sliceable.run(rowsParser)(rowsFile).fold(println, println)

  val tempHeader: Parser[Parser[Double]] = Sliceable.string("Temperature").map(_ => tempParser)
  val dateHeader: Parser[Parser[Date]] = Sliceable.string("Date").map(_ => dateParser)

  def headerParser[A, B, C](header1: Parser[Parser[A]], header2: Parser[Parser[B]])(f: (A, B) => C): Parser[Parser[C]] =
    parserMonad.map2(header1 <* ",", header2) { (header1Parser, header2Parser) =>
      parserMonad.map2(header1Parser <* ",", header2Parser)(f)
    }

  val header: Parser[Parser[Row]] = token(
    "#" *> (
      headerParser(tempHeader, dateHeader)((temp, date) => Row(date, temp))
        .or(headerParser(dateHeader, tempHeader)(Row.apply))
    )
  )

  val rowsParser2: Parser[List[Row]] =
    root(whitespace *> parserMonad.flatMap(header)(parser => many(parser <* whitespace)))

  val rowsFile2 =
    """
      |# Temperature, Date
      |25, 1/1/2010
      |28, 2/1/2010
      |42, 3/1/2010
      |53, 4/1/2010
    """.trim.stripMargin

  Sliceable.run(rowsParser2)(rowsFile2).fold(println, println)

  val rowsFile3 =
    """
      |# Date, Temperature
      |1/1/2010, 25
      |2/1/2010, 28
      |3/1/2010, 42
      |4/1/2010, 53
    """.trim.stripMargin

  Sliceable.run(rowsParser2)(rowsFile3).fold(println, println)

  val lazyListApplicative = new Applicative[LazyList]:
    // The infinite, constant stream
    def unit[A](a: => A): LazyList[A] = LazyList.continually(a)
    // Combine elements pointwise
    override def map2[A, B, C](a: LazyList[A], b: LazyList[B])(f: (A, B) => C): LazyList[C] = a zip b map f.tupled

  def printLazyList[A](l: Iterable[A]): Unit =
    l.map(i => s"$i ").foreach(print)
    println()

  printLazyList(lazyListApplicative.map2(LazyList(1, 2, 3), LazyList(4, 5))(_ + _))
  printLazyList(lazyListMonad.map2(LazyList(1, 2, 3), LazyList(4, 5))(_ + _))

  // Exercise 12.4
  printLazyList(lazyListApplicative.sequence(List(LazyList(1, 2, 3), LazyList(4, 5))))
  printLazyList(lazyListMonad.sequence(List(LazyList(1, 2, 3), LazyList(4, 5))))

  // Exercise 12.5

  def eitherMonad[E]: Monad[[A] =>> Either[E, A]] = new Monad:
    def unit[A](a: => A): Either[E, A] = Right(a)
    override def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)

  def validName(name: String): Validation[String, String] =
    if name != "" then Success(name)
    else Failure("Name cannot be empty")

  def validBirthdate(birthdate: String): Validation[String, Date] =
    try Success(parseDate(birthdate, "yyyy-MM-dd"))
    catch case _: Exception => Failure("Birthdate must be in the form yyyy-MM-dd")

  def validPhone(phoneNumber: String): Validation[String, String] =
    if phoneNumber.matches("[0-9]{10}") then Success(phoneNumber)
    else Failure("Phone number must be 10 digits")

  def validWebForm(name: String, birthdate: String, phone: String): Validation[String, WebForm] =
    validationApplicative.map3(
      validName(name),
      validBirthdate(birthdate),
      validPhone(phone)
    )(WebForm.apply)

  def format(e: Option[Employee], pay: Option[Pay]): Option[String] = optionMonad.map2(e, pay) { (e, pay) =>
    s"${e.name} makes ${pay.rate * pay.hoursPerYear}"
  }

  val e = Some(Employee("walle", 1))
  val pay = Some(Pay(1, 8))
  println(format(e, pay))

  def formatV2(name: Option[String], pay: Option[Double]): Option[String] = optionMonad.map2(name, pay) { (e, pay) =>
    s"$e makes $pay"
  }

  println(formatV2(optionMonad.map(e)(_.name), optionMonad.map(pay)(pay => pay.rate * pay.hoursPerYear)))

  // Exercise 12.13

  val listTraverse: Traverse[List] = new Traverse[List]:
    override def traverse[G[_], A, B](fa: List[A])(f: A => G[B])(using G: Applicative[G]): G[List[B]] =
      fa.foldRight(G.unit(List.empty[B]))((a, fbs) => G.map2(f(a), fbs)(_ :: _))

  val optionTraverse: Traverse[Option] = new Traverse[Option]:
    override def traverse[G[_], A, B](fa: Option[A])(f: A => G[B])(using G: Applicative[G]): G[Option[B]] = fa match
      case Some(a) => G.map(f(a))(Some(_))
      case None => G.unit(None)

  val treeTraverse: Traverse[Tree] = new Traverse[Tree]:
    override def traverse[G[_], A, B](fa: Tree[A])(f: A => G[B])(using G: Applicative[G]): G[Tree[B]] =
      G.map2(f(fa.head), listTraverse.traverse(fa.tail)(a => traverse(a)(f)))(Tree(_, _))

end ApplicativeApp
