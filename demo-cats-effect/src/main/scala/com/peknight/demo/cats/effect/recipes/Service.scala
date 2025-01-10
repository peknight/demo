package com.peknight.demo.cats.effect.recipes

import cats.effect.IO

trait Service:
  def query(): IO[ServiceResponse]
