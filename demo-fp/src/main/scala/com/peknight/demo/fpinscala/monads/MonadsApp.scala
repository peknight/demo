package com.peknight.demo.fpinscala.monads

object MonadsApp extends App {
  val listFunctor = new Functor[List] {
    def map[A, B](as: List[A])(f: A => B): List[B] = as map f
  }
}

