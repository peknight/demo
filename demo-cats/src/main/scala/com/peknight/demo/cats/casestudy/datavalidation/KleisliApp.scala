package com.peknight.demo.cats.casestudy.datavalidation

import cats.data.{Kleisli, NonEmptyList}
import cats.syntax.apply._

object KleisliApp extends App {
  type Errors = NonEmptyList[String]
  type Result[A] = Either[Errors, A]
  type Check[A, B] = Kleisli[Result, A, B]

  def check[A, B](func: A =>  Result[B]): Check[A, B] = Kleisli(func)

  def checkPred[A](pred: Predicate[Errors, A]): Check[A, A] = Kleisli[Result, A, A](pred.run)

  def error(s: String): NonEmptyList[String] = NonEmptyList(s, Nil)

  def longerThan(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"Must be longer than $n characters"),
    _.size > n
  )

  val alphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must be all alphanumeric characters"),
    _.forall(_.isLetterOrDigit)
  )

  def contains(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char"),
    _.contains(char)
  )

  def containsOnce(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char only once"),
    str => str.filter(c => c == char).size == 1
  )

  val checkUsername: Check[String, String] = checkPred(longerThan(3) and alphanumeric)

  val splitEmail: Check[String, (String, String)] = check(_.split('@') match {
    case Array(name, domain) =>
      Right((name, domain))
    case _ =>
      Left(error("Must contain a single @ character"))
  })

  val checkLeft: Check[String, String] = checkPred(longerThan(0))

  val checkRight: Check[String, String] = checkPred(longerThan(3) and contains('.'))

  val joinEmail: Check[(String, String), String] = check {
    case (l, r) => (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
  }

  val checkEmail: Check[String, String] = splitEmail andThen joinEmail

  def createUser(username: String, email: String): Either[Errors, User] =
    (checkUsername.run(username), checkEmail.run(email)).mapN(User.apply)

  println(createUser("Noel", "noel@underscore.io"))
  println(createUser("", "dave@underscore.io@io"))

}
