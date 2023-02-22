package com.peknight.demo.scalacheck.scalatest

class Counter:
  private var c = 0
  def reset() = c = 0
  def click() = c += 1
  def enter(n: Int) = c = n
  def count = c
