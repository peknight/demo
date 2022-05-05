package com.peknight.demo.js.common.state

import cats.data.State

object StateOps:

  def modifyAndGet[S](f: S => S): State[S, S] = State { s =>
    val next = f(s)
    (next, next)
  }
