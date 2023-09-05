package com.peknight.demo.catseffect.recipes

import cats.effect.IO

trait Service:
  def query(): IO[ServiceResponse]
