package com.peknight.demo.cats.functor

import cats.{Contravariant, Show}
import cats.syntax.contravariant._
import com.peknight.demo.cats.introduction.Printable.format
import com.peknight.demo.cats.introduction.PrintableInstances._

object ContravariantApp extends App {
  println(format("hello"))
  println(format(true))
  println(format(Box("hello world")))
  println(format(Box(true)))
  println(format(Box(123)))


  val showString = Show[String]

  val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")

  println(showSymbol.show(Symbol("dave")))
  println(showString.contramap[Symbol](sym => s"'${sym.name}").show(Symbol("dave")))


}
