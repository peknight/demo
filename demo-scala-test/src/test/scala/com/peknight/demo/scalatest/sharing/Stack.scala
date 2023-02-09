package com.peknight.demo.scalatest.sharing

import scala.collection.mutable.ListBuffer

class Stack[T]:
  val MAX = 10
  private val buf = new ListBuffer[T]

  def push(o: T): Unit =
    if !full then buf.prepend(o) else throw new IllegalStateException("can't push onto a full stack")
  def pop(): T =
    if !empty then buf.remove(0) else throw new IllegalStateException("can't pop an empty stack")
  def peek: T =
    if !empty then buf.head else throw new IllegalStateException("can't peek an empty stack")

  def full: Boolean = buf.size == MAX
  def empty: Boolean = buf.isEmpty
  def size = buf.size

  override def toString = buf.mkString("Stack(", ", ", ")")