package com.peknight.demo.scala3.traits

trait Incrementing extends IntQueue:
  abstract override def put(x: Int) = super.put(x + 1)
