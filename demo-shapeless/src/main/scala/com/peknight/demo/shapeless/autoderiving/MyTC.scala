package com.peknight.demo.shapeless.autoderiving

import shapeless3.deriving.K0

trait MyTC[A]

object MyTC:
  given MyTC[Int] with
    ???

  given MyTC[String] with
    ???

  given MyTC[Boolean] with
    ???

  given myTCSum[A](using inst: => K0.CoproductInstances[MyTC, A]): MyTC[A] with
    ???

  given myTCProduct[A](using inst: => K0.ProductInstances[MyTC, A]): MyTC[A] with
    ???

  inline def derived[A](using gen: K0.Generic[A]): MyTC[A] = gen.derive(myTCProduct, myTCSum)
