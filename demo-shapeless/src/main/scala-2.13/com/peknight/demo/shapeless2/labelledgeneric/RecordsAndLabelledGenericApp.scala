package com.peknight.demo.shapeless2.labelledgeneric

import shapeless.HNil
import shapeless.syntax.singleton._

object RecordsAndLabelledGenericApp extends App {
  val garfield = ("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil
  println(garfield)
}
