package com.peknight.demo.playground.app

import cats.effect.{IO, IOApp}

object PlaygroundApp extends IOApp.Simple :
  def run = IO.println("Hello, world!")
