package com.peknight.demo.zio.gettingstarted

import zio.*

import java.io.IOException

object ConsoleApp extends ZIOAppDefault:

  def run: IO[IOException, Unit] =
    for
      _ <- Console.print("Hello World")
      _ <- Console.printLine("Hello World")
      _ <- echo
    yield ()

  val echo: IO[IOException, Unit] = Console.readLine.flatMap(line => Console.printLine(line))
