package com.peknight.demo.scalatest.either

import org.scalatest.EitherValues.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class EitherValuesFunSuite extends AnyFunSuite:
  test("either should be left or right and that its value should meet some expectation.") {
    val either1 = Right(16)
    val either2 = Left("Muchas problemas")
    either1.value should be > 9
    either2.left.value should be ("Muchas problemas")
    assert(either1.value > 9)
    assert(either2.left.value === "Muchas problemas")
  }

  test("it would throw a NoSuchElementException") {
    // val either: Either[String, Int] = Left("Muchas problemas")
    val either: Either[String, Int] = Right(16)
    either.toOption.get should be > 9
  }

  test("To get a stack depth exception you would need to make two statements") {
    // val either: Either[String, Int] = Left("Muchas problemas")
    val either: Either[String, Int] = Right(16)
    either should be (Symbol("right"))
    either.toOption.get should be > 9
  }

  test("EitherValues trait allows you to state that more concisely") {
    // val either: Either[String, Int] = Left("Muchas problemas")
    val either: Either[String, Int] = Right(16)
    either.value should be > 9
  }
