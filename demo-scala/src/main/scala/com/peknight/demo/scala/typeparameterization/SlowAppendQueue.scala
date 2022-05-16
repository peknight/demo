package com.peknight.demo.scala.typeparameterization

class SlowAppendQueue[T](elems: List[T]):
  def head = elems.head
  def tail = new SlowAppendQueue(elems.tail)
  def enqueue(x: T) = SlowAppendQueue(elems ::: List(x))

