package com.peknight.demo.shapeless.functionaloperations

trait Poly:
  def apply[A](arg: A)(using cse: Case[this.type, A]): cse.Result = cse.apply(arg)
