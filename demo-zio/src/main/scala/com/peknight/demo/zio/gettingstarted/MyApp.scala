package com.peknight.demo.zio.gettingstarted

import zio.*
import zio.Console.*

import java.io.IOException

object MyApp extends ZIOAppDefault:

  def run: ZIO[Any, IOException, Unit] = myAppLogic

  def myAppLogic: ZIO[Any, IOException, Unit] =
    for
      _ <- printLine("Hello! What is your name?")
      name <- readLine
      _ <- printLine(s"Hello, $name, welcome to ZIO!")
    yield ()

