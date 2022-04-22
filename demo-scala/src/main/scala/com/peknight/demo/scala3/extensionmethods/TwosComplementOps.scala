package com.peknight.demo.scala3.extensionmethods

object TwosComplementOps:
  extension [N](n: N)(using tc: TwosComplement[N])
    def isMinValue: Boolean = tc.equalsMinValue(n)
    def absOption: Option[N] =
      if !isMinValue then Some(tc.absOf(n)) else None
    def negateOption: Option[N] =
      if !isMinValue then Some(tc.negationOf(n)) else None


