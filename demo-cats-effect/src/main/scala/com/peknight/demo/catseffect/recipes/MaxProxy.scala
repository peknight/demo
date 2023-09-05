package com.peknight.demo.catseffect.recipes

import cats.effect.IO
import cats.effect.std.AtomicCell

class MaxProxy(atomicCell: AtomicCell[IO, Double], requestService: Service):
  def queryCache(): IO[ServiceResponse] =
    atomicCell.evalModify { current =>
      requestService.query().map { result =>
        if result.exchangeRate > current then (result.exchangeRate, result)
        else (current, result)
      }
    }

  def getHistoryMax(): IO[Double] = atomicCell.get

