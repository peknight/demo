package com.peknight.demo.shapeless.functionaloperations

trait Case[P, A] {
  type Result
  def apply(a: A): Result
}
