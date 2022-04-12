package com.peknight.demo.fs2.gettingstarted

import cats.effect.{IO, IOApp}
import fs2.io.file.{Files, Path}
import fs2.{Stream, text}

object Converter extends IOApp.Simple {
  val converter: Stream[IO, Unit] = {
    def fahrenheitToCelsius(f: Double): Double = (f - 32) * (5.0 / 9.0)
    Files[IO].readAll(Path("demo-fs2/src/main/resources/com/peknight/demo/fs2/gettingstarted/fahrenheit.txt"))
      .through(text.utf8.decode)
      .through(text.lines)
      .filter(s => s.trim.nonEmpty && !s.startsWith("//"))
      .map(line => fahrenheitToCelsius(line.toDouble).toString)
      .intersperse("\n")
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path("demo-fs2/src/main/resources/com/peknight/demo/fs2/gettingstarted/celsius.txt")))
  }

  def run: IO[Unit] = converter.compile.drain
}
