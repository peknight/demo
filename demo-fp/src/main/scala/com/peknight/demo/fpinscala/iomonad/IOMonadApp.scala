package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.Console.{ConsoleIO, printLn, readLn, runConsoleFunction0, runConsolePar}
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import java.util.concurrent.Executors
import scala.io.StdIn.readLine
import scala.language.implicitConversions

object IOMonadApp extends scala.App:

  def contestV1(p1: Player, p2: Player): Unit =
    if p1.score > p2.score then println(s"${p1.name} is the winner!")
    else if p2.score > p1.score then println(s"${p2.name} is the winner!")
    else println("It's a draw.")

  def winner(p1: Player, p2: Player): Option[Player] =
    if p1.score > p2.score then Some(p1)
    else if p2.score > p1.score then Some(p2)
    else None

  def contestV2(p1: Player, p2: Player): Unit = winner(p1, p2) match
    case Some(Player(name, _)) => println(s"$name is the winner!")
    case None => println("It's a draw.")

  def winnerMsg(p: Option[Player]): String = p map {
    case Player(name, _) => s"$name is the winner!"
  } getOrElse "It's a draw."

  def contestV3(p1: Player, p2: Player): Unit = println(winnerMsg(winner(p1, p2)))

  def contest(p1: Player, p2: Player): IO[Unit] = IO.PrintLine(winnerMsg(winner(p1, p2)))

  def fahrenheitToCelsius(f: Double): Double = (f - 32) * 5.0 / 9.0

  def converterV1: Unit =
    println("Enter a temperature in degrees Fahrenheit: ")
    val d = readLine().toDouble
    println(fahrenheitToCelsius(d))

  def converter: IO[Unit] =
    for
      _ <- IO.PrintLine("Enter a temperature in degrees Fahrenheit: ")
      d <- IO.ReadLine.map(_.toDouble)
      _ <- IO.PrintLine(fahrenheitToCelsius(d).toString)
    yield ()

  def factorial(n: Int): IO[Int] =
    for
      acc <- IO.ref(1)
      _ <- IO.foreachM(1 to n to(LazyList): LazyList[Int])(i => acc.modify(_ * i).skip)
      result <- acc.get
    yield result

  val helpString =
    """
      | The Amazing Factorial REPL, v2.0
      | q - quit
      | <number> - compute the factorial of the given number
      | <anything else> - bomb with horrible error
    """.trim.stripMargin

  val factorialREPL: IO[Unit] = IO.sequence_ {
    IO { println(helpString) }
    IO.doWhile (IO(readLine())) { line =>
      IO.when (line != "q") {
        for
          n <- factorial(line.toInt)
          _ <- IO { println("factorial: " + n) }
        yield ()
      }
    }
  }

  val stillGoing = IO.forever(IO.PrintLine("Still going..."))
  // 这里Debug看下 可以更好理解
  // IO.run(stillGoing)

  // StackOverflowError
  // val f = (x: Int) => x
  // val g = List.fill(100000)(f).foldLeft(f)(_ compose _)
  // println(g(42))

  val f: Int => IO[Int] = (x: Int) => IO.Return(x)
  val g = List.fill(100000)(f).foldLeft(f)((a, b) => x => IO(()).flatMap(_ => a(x).flatMap(b)))
  val x1 = IO.run(g(0))
  println(x1)
  val x2 = IO.run(g(42))
  println(x2)

  val f1: Free[Console, Option[String]] =
    for
      _ <- printLn("I can only interact with the console.")
      ln <- readLn
    yield ln

  val S = Executors.newFixedThreadPool(2)

  println(Par.run(S)(runConsolePar(f1)))

  println(runConsoleFunction0(f1)())

  def p: ConsoleIO[Unit] =
    for
      _ <- printLn("What's your name?")
      n <- readLn
      _ <- n match
        case Some(n) => printLn(s"Hello, $n!")
        case None => printLn(s"Fine, be that way.")
    yield ()

  Par.run(S)(runConsolePar(p))

  S.shutdown()

end IOMonadApp
