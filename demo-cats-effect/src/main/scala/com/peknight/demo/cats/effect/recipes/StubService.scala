package com.peknight.demo.cats.effect.recipes

import cats.effect.IO
import cats.effect.std.Random

object StubService extends Service:
  def query(): IO[ServiceResponse] =
    Random.scalaUtilRandom[IO].flatMap(random => random.nextDouble).map(ServiceResponse.apply)
