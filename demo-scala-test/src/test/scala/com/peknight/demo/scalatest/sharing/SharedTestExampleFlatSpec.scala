package com.peknight.demo.scalatest.sharing

import org.scalatest.flatspec.AnyFlatSpec

class SharedTestExampleFlatSpec extends AnyFlatSpec with StackBehaviors:
  // Stack fixture creation methods
  def emptyStack = new Stack[Int]

  def fullStack =
    val stack = new Stack[Int]
    for i <- 0 until stack.MAX do stack.push(i)
    stack

  def stackWithOneItem =
    val stack = new Stack[Int]
    stack.push(9)
    stack

  def stackWithOneItemLessThanCapacity =
    val stack = new Stack[Int]
    for i <- 1 to 9 do stack.push(i)
    stack

  val lastValuePushed = 9

  "A Stack (when empty)" should "be empty" in {
    assert(emptyStack.empty)
  }

  it should "complain on peek" in {
    intercept[IllegalStateException](emptyStack.peek)
  }

  it should "complain on pop" in {
    intercept[IllegalStateException](emptyStack.pop())
  }

  "A Stack (with one item)" should behave like nonEmptyStack(stackWithOneItem, lastValuePushed)

  it should behave like nonFullStack(stackWithOneItem)

  "A Stack (with one item less than capacity)" should
    behave like nonEmptyStack(stackWithOneItemLessThanCapacity, lastValuePushed)

  it should behave like nonFullStack(stackWithOneItemLessThanCapacity)

  "A Stack (full)" should "be full" in {
    assert(fullStack.full)
  }

  it should behave like nonEmptyStack(fullStack, lastValuePushed)

  it should "complain on a push" in {
    intercept[IllegalStateException](fullStack.push(10))
  }