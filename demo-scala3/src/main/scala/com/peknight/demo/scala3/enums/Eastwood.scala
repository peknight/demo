package com.peknight.demo.scala3.enums

enum Eastwood[+G, +B]:
  def map[G2](f: G => G2): Eastwood[G2, B] =
    this match
      case Good(g) => Good(f(g))
      case Bad(b) => Bad(b)
      case Ugly(ex) => Ugly(ex)

  case Good(g: G)
  case Bad(b: B)
  case Ugly(ex: Throwable)
