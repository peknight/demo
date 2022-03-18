package com.peknight.demo.fpinscala.parsing

import com.peknight.demo.fpinscala.parsing.Result.{Failure, Slice, Success}

sealed trait Result[+A] {
  def extract(input: String): Either[ParseError, A]
  def slice: Result[String]

  def uncommit: Result[A] = this match {
    case Failure(e, true) => Failure(e, false)
    case _ => this
  }

  def addCommit(isCommitted: Boolean): Result[A] = this match {
    case Failure(e, c) => Failure(e, c || isCommitted)
    case _ => this
  }

  def mapError(f: ParseError => ParseError): Result[A] = this match {
    case Failure(e, c) => Failure(f(e), c)
    case _ => this
  }

  def advanceSuccess(n: Int): Result[A]
}

object Result {
  case class Slice(length: Int) extends Result[String] {
    def extract(input: String): Either[ParseError, String] = Right(input.substring(0, length))
    def slice: Result[String] = this
    def advanceSuccess(n: Int): Result[String] = Slice(length + n)
  }
  case class Success[+A](get: A, length: Int) extends Result[A] {
    def extract(input: String): Either[ParseError, A] = Right(get)
    def slice: Result[String] = Slice(length)
    def advanceSuccess(n: Int): Result[A] = Success(get, length + n)
  }
  case class Failure(get: ParseError, isCommitted: Boolean) extends Result[Nothing] {
    def extract(input: String): Either[ParseError, Nothing] = Left(get)
    def slice: Result[String] = this
    def advanceSuccess(n: Int): Result[Nothing] = this
  }

  def firstNonmatchingIndex(s1: String, s2: String, offset: Int): Int = {
    s1.substring(offset)
      .zip(s2)
      .zipWithIndex
      .find { case ((c1, c2), _) => c1 != c2 }
      .map { case ((_, _), index) => index }
      .getOrElse { if (s1.length - offset >= s2.length) -1 else s1.length - offset }
  }
}
