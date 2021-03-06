package com.peknight.demo.cats.monad

sealed trait LoginError extends Product with Serializable
object LoginError {
  final case class UserNotFound(username: String) extends LoginError
  final case class PasswordIncorrect(username: String) extends LoginError
  case object UnexpectedError extends LoginError
}


