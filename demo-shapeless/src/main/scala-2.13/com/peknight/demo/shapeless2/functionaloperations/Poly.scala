package com.peknight.demo.shapeless2.functionaloperations

trait Poly {
  def apply[A](arg: A)(implicit cse: Case[this.type, A]): cse.Result = cse.apply(arg)
}
