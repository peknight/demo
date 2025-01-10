package com.peknight.demo.cats.effect.recipes

import cats.effect.IO
import cats.effect.std.AtomicCell

class Server(atomicCell: AtomicCell[IO, Int]):
  def update(input: Int): IO[Int] = IO(input + 1)
  def performUpdate(): IO[Int] = atomicCell.evalGetAndUpdate(i => update(i))
