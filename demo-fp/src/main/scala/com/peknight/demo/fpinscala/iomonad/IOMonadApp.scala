package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.IO._

import scala.io.StdIn.readLine

object IOMonadApp extends App {
  def contestV1(p1: Player, p2: Player): Unit = {
    if (p1.score > p2.score) println(s"${p1.name} is the winner!")
    else if (p2.score > p1.score) println(s"${p2.name} is the winner!")
    else println("It's a draw.")
  }

  def winner(p1: Player, p2: Player): Option[Player] =
    if (p1.score > p2.score) Some(p1)
    else if (p2.score > p1.score) Some(p2)
    else None

  def contestV2(p1: Player, p2: Player): Unit = winner(p1, p2) match {
    case Some(Player(name, _)) => println(s"$name is the winner!")
    case None => println("It's a draw.")
  }

  def winnerMsg(p: Option[Player]): String = p map {
    case Player(name, _) => s"$name is the winner!"
  } getOrElse "It's a draw."

  def contestV3(p1: Player, p2: Player): Unit = println(winnerMsg(winner(p1, p2)))

  def contest(p1: Player, p2: Player): IO[Unit] = PrintLine(winnerMsg(winner(p1, p2)))

  def fahrenheitToCelsius(f: Double): Double = (f - 32) * 5.0 / 9.0

  def converterV1: Unit = {
    println("Enter a temperature in degrees Fahrenheit: ")
    val d = readLine().toDouble
    println(fahrenheitToCelsius(d))
  }

  def converter: IO[Unit] = for {
    _ <- PrintLine("Enter a temperature in degrees Fahrenheit: ")
    d <- ReadLine.map(_.toDouble)
    _ <- PrintLine(fahrenheitToCelsius(d).toString)
  } yield ()

  def factorial(n: Int): IO[Int] = for {
    acc <- ref(1)
    _ <- foreachM(1 to n to(LazyList): LazyList[Int])(i => acc.modify(_ * i).skip)
    result <- acc.get
  } yield result

  val helpstring =
    """
      | The Amazing Factorial REPL, v2.0
      | q - quit
      | <number> - compute the factorial of the given number
      | <anything else> - bomb with horrible error
      """.trim.stripMargin

  val factorialREPL: IO[Unit] = sequence_ {
    IO { println(helpstring) }
    doWhile (IO(readLine())) { line =>
      when (line != "q") { for {
        n <- factorial(line.toInt)
        _ <- IO { println("factorial: " + n) }
      } yield () }
    }
  }

  val stillGoing = IO.forever(PrintLine("Still going..."))
  // 这里Debug看下 可以更好理解
  // IO.run(stillGoing)
}
