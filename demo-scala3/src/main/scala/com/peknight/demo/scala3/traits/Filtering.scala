package com.peknight.demo.scala3.traits

trait Filtering(f: Int => Boolean) extends IntQueue:
  abstract override def put(x: Int): Unit = if f(x) then super.put(x)
