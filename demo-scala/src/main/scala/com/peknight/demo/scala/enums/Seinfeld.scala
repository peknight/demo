package com.peknight.demo.scala.enums

enum Seinfeld[+E]:
  def ::[E2 >: E](o: E2): Seinfeld[E2] = Yada(o, this)
  case Yada(head: E, tail: Seinfeld[E])
  case Nada
