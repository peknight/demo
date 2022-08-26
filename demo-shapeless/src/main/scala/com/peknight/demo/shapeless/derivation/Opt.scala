package com.peknight.demo.shapeless.derivation

enum Opt[+T] derives Eq, EqShapeless:
  case Sm(t: T)
  case Nn

