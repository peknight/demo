package com.peknight.demo.scala.traits

trait Doubling extends IntQueue:
  abstract override def put(x: Int) = super.put(2 * x)