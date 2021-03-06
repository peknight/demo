package com.peknight.demo.cats.monad

import cats.syntax.either._
import com.peknight.demo.cats.monad.LoginError.{PasswordIncorrect, UnexpectedError, UserNotFound}

object LoginApp extends App {
  type LoginResult = Either[LoginError, User]

  def handleError(error: LoginError): Unit = error match {
    case UserNotFound(u) => println(s"User not found: $u")
    case PasswordIncorrect(u) => println(s"Password incorrect: $u")
    case UnexpectedError => println(s"Unexpected error")
  }

  val result1: LoginResult = User("dave", "passw0rd").asRight
  val result2: LoginResult = UserNotFound("dave").asLeft

  result1.fold(handleError, println)
  result2.fold(handleError, println)
}
