package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.state.{RNG, State}

trait Cogen[-A]:
  def sample(a: A, rng: RNG): RNG

object Cogen:

  def fn[A, B](in: Cogen[A])(out: Gen[B]): Gen[A => B] = Gen {
    State { (rng: RNG) =>
      val f = (a: A) => out.sample.run(in.sample(a, rng))._1
      (f, rng)
    }
  }