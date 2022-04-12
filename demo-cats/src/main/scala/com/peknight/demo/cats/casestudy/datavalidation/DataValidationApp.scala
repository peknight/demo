package com.peknight.demo.cats.casestudy.datavalidation

import cats.data.Kleisli
import cats.syntax.either._
import cats.syntax.validated._

object DataValidationApp extends App {
  val aCheckF: CheckF[List[String], Int] = CheckF { v =>
    if (v > 2) v.asRight
    else List("Must be > 2").asLeft
  }

  val bCheckF: CheckF[List[String], Int] = CheckF { v =>
    if (v < -2) v.asRight
    else List("Must be < -2").asLeft
  }

  val checkF: CheckF[List[String], Int] = aCheckF and bCheckF

  println(checkF(5))
  println(checkF(0))

  val a: OldCheck[List[String], Int] = OldCheck.pure { v =>
    if (v > 2) v.valid
    else List("Must be > 2").invalid
  }

  val b: OldCheck[List[String], Int] = OldCheck.pure { v =>
    if (v < -2) v.valid
    else List("Must be < -2").invalid
  }

  val check: OldCheck[List[String], Int] = a and b

  println(check(5))
  println(check(0))

  println(User.createUser("Noel", "noel@underscore.io"))
  println(User.createUser("", "dave@underscore.io@io"))

  val step1: Kleisli[List, Int, Int] = Kleisli(x => List(x + 1, x - 1))

  val step2: Kleisli[List, Int, Int] = Kleisli(x => List(x, -x))

  val step3: Kleisli[List, Int, Int] = Kleisli(x => List(x * 2, x / 2))

  val pipeline = step1 andThen step2 andThen step3

  println(pipeline.run(20))
}
