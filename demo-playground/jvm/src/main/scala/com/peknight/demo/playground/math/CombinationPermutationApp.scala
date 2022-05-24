package com.peknight.demo.playground.math

import cats.effect.{IO, IOApp}
import cats.syntax.traverse.*
import fs2.Stream

object CombinationPermutationApp extends IOApp.Simple:
  def run =
    for
      _ <- Stream.fromIterator[IO](List(1, 2, 3, 4, 5).combinations(2), 8).evalMap(IO.println).compile.drain
      _ <- Stream.fromIterator[IO](List(1, 2, 3, 4, 5).permutations, 8).evalMap(IO.println).compile.drain
    yield ()

