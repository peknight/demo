package com.peknight.demo.scala.derivation

enum Opt[+T]:
  case Sm(t: T)
  case Nn

