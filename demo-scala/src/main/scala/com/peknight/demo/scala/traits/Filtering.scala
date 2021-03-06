package com.peknight.demo.scala.traits

transparent trait Filtering(f: Int => Boolean) extends IntQueue:
  abstract override def put(x: Int): Unit = if f(x) then super.put(x)
