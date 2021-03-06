package com.peknight.demo.cats.monad

import cats.data.State

object StateApp extends App {

  val a = State[Int, String] { state =>
    (state, s"The state is $state")
  }
}
