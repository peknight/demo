package com.peknight.demo.cats.monad

import cats.syntax.either.*

object EitherApp extends App:
  val either1: Either[String, Int] = Right(10)
  val either2: Either[String, Int] = Right(32)

  //  for
  //    a <- either1.right
  //    b <- either2.right
  //  yield a + b

  for
    a <- either1
    b <- either2
  yield a + b

  val aEither = 3.asRight[String]
  val bEither = 4.asRight[String]

  val eitherAns =
    for
      x <- aEither
      y <- bEither
    yield x * x + y * y

  println(eitherAns)

  def countPositive(nums: List[Int]) = nums.foldLeft(0.asRight[String]) { (accumulator, num) =>
    if num > 0 then accumulator.map(_ + 1)
    else Left("Negative. Stopping!")
  }

  println(countPositive(List(1, 2, 3)))
  println(countPositive(List(1, -2, 3)))

  println(Either.catchOnly[NumberFormatException]("foo".toInt))
  println(Either.catchNonFatal(sys.error("Badness")))
  println(Either.fromTry(scala.util.Try("foo".toInt)))
  println(Either.fromOption[String, Int](None, "Badness"))

  println("Error".asLeft[Int].getOrElse(0))
  println("Error".asLeft[Int].orElse(2.asRight[String]))

  println(-1.asRight[String].ensure("Must be non-negative!")(_ > 0))

  println("error".asLeft[Int].recover { case _: String => -1 })
  println("error".asLeft[Int].recoverWith { case _: String => Right(-1) })

  println("foo".asLeft[Int].leftMap(_.reverse))
  println(6.asRight[String].bimap(_.reverse, _ * 7))
  println("bar".asLeft[Int].bimap(_.reverse, _ * 7))

  println(123.asRight[String])
  println(123.asRight[String].swap)


  val divAns =
    for
      a <- 1.asRight[String]
      b <- 0.asRight[String]
      c <- if b == 0 then "DIV0".asLeft[Int] else (a / b).asRight[String]
    yield c * 100
  println(divAns)

  type Result[A] = Either[Throwable, A]
