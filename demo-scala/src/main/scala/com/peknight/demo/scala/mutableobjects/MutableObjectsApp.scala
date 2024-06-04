package com.peknight.demo.scala.mutableobjects

import com.peknight.demo.scala.mutableobjects.MySimulation.*

object MutableObjectsApp extends App:
  val input1, input2, sum, carry = new Wire
  probe("sum", sum)
  probe("carry", carry)
  halfAdder(input1, input2, sum, carry)
  input1.setSignal(true)
  run()
  input2.setSignal(true)
  run()


