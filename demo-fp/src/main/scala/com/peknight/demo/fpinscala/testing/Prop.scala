package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.testing.Prop.{FailedCase, SuccessCount}

trait Prop {

  def check: Either[(FailedCase, SuccessCount), SuccessCount]

  def &&(p: Prop): Prop = new Prop {
    def check: Either[(FailedCase, SuccessCount), SuccessCount] = ???
  }
}
object Prop {
  // Type aliases like this can help the readability of an API
  type FailedCase = String
  type SuccessCount = Int
}
