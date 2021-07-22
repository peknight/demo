package com.peknight.demo.fpinscala.testing

case class SGen[+A](forSize: Int => Gen[A]) {
  def apply(n: Int): Gen[A] = forSize(n)

  def map[B](f: A => B): SGen[B] = SGen { forSize(_) map f }

  def flatMap[B](f: A => SGen[B]): SGen[B] = {
    val g2: Int => Gen[B] = n => {
      forSize(n) flatMap { f(_).forSize(n) }
    }
    SGen(g2)
  }

  def **[B](s2: SGen[B]): SGen[(A, B)] = SGen(n => apply(n) ** s2(n))
}
