package com.peknight.demo.scalacheck.concepts

class Counter:
  private var n = 0
  def inc = n += 1
  // def dec = n -= 1
  // introduce a bug
  def dec = if n > 3 then n -= 2 else n -= 1
  def get = n
  def reset = n = 0