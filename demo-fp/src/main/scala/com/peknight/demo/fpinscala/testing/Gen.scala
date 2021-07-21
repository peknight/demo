package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.state.{RNG, State}
import org.scalacheck.Prop

case class Gen[A](sample: State[RNG, A])

object Gen {
  def listOf[A](a: Gen[A]): Gen[List[A]] = ???

  def listOfN[A](n: Int, a: Gen[A]): Gen[List[A]] = ???

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = ???

  def choose(start: Int, stopExclusive: Int): Gen[Int] = ???
}
