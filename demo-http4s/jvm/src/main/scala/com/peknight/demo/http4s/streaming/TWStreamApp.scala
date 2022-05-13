package com.peknight.demo.http4s.streaming

import cats.effect.*

object TWStreamApp extends IOApp.Simple:

  def run = new TWStream[IO].run

