package com.peknight.demo.scalatest.tags

import org.scalatest.flatspec.AnyFlatSpec
import scala.collection.mutable.Stack

class StackFlatSpec extends AnyFlatSpec:
  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() === 2)
    assert(stack.pop() === 1)
  }

  ignore should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[String]
    intercept[NoSuchElementException](emptyStack.pop())
  }
