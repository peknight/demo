package com.peknight.demo.fpinscala.testing

import org.scalacheck.{Gen, Prop}

object Gen {
  def listOf[A](a: Gen[A]): Gen[List[A]] = ???

  def listOfN[A](n: Int, a: Gen[A]): Gen[List[A]] = ???

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = ???
}
