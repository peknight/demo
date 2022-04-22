package com.peknight.demo.scala3.typeparameterization

/**
 * @param smele elems反过来的意思
 */
class SlowHeadQueue[T](smele: List[T]):
  def head = smele.last
  def tail = new SlowHeadQueue(smele.init)
  def enqueue(x: T) = SlowHeadQueue(x :: smele)
