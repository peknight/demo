package com.peknight.demo.shapeless2.functionaloperations

trait Case[P, A] {
  type Result
  def apply(a: A): Result
}
