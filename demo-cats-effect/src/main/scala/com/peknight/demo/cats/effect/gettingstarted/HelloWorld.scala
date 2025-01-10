package com.peknight.demo.cats.effect.gettingstarted

import cats.effect.{IO, IOApp}

object HelloWorld extends IOApp.Simple:
  val run = IO.println("Hello, World!")
