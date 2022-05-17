package com.peknight.demo.doobie.custommappings

import doobie.{Get, Meta, Put}

object NatModule:
  sealed trait Nat derives CanEqual
  case object Zero extends Nat
  case class Succ(n: Nat) extends Nat

  def toInt(n: Nat): Int =
    def go(n: Nat, acc: Int): Int =
      n match
        case Zero => acc
        case Succ(n) => go(n, acc + 1)
    go(n, 0)

  def fromInt(n: Int): Nat =
    def go(n: Int, acc: Nat): Nat =
      if n <= 0 then acc else go(n - 1, Succ(acc))
    go(n, Zero)

  val natGet: Get[Nat] = Get[Int].map(fromInt)
  val natPut: Put[Nat] = Put[Int].contramap(toInt)
  val natGet2: Get[Nat] = Get[Int].tmap(fromInt)
  val natPut2: Put[Nat] = Put[Int].tcontramap(toInt)
  val natMeta: Meta[Nat] = Meta[Int].imap(fromInt)(toInt)
  given natMeta2: Meta[Nat] = Meta[Int].timap(fromInt)(toInt)

