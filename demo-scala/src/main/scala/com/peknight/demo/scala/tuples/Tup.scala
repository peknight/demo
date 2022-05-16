package com.peknight.demo.scala.tuples

enum Tup:
  case EmpT
  case TCons[H, T <: Tup](head: H, tail: T)

object Tup:

//  def concat[L <: Tup, R <: Tup](left: L, right: R): Tup =
//    left match
//      case EmpT => right
//      case TCons(head, tail) => TCons(head, concat(tail, right))

  extension [A, T <: Tup] (a: A)
    def *: (t: T): TCons[A, T] = TCons(a, t)

  type Concat[L <: Tup, R <: Tup] <: Tup = L match
    case EmpT.type => R
    case TCons[headType, tailType] => TCons[headType, Concat[tailType, R]]

  def concat[L <: Tup, R <: Tup](left: L, right: R): Concat[L, R] =
    left match
      case _: EmpT.type => right
      case cons: TCons[_, _] => TCons(cons.head, concat(cons.tail, right))