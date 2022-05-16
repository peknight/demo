package com.peknight.demo.scala.traits

import scala.collection.mutable.ArrayBuffer

class BasicIntQueue extends IntQueue:
  private val buf = ArrayBuffer.empty[Int]
  def get() = buf.remove(0)
  def put(x: Int) = buf += x
