package com.peknight.demo.scalatest.style

class TVSet:
  private var on: Boolean = false
  def isOn: Boolean = on
  def pressPowerButton(): Unit = on = !on
