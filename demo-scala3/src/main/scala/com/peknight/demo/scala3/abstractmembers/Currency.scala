package com.peknight.demo.scala3.abstractmembers

abstract class Currency:
  val amount: Long
  def designation: String
  override def toString = s"$amount $designation"
  def + (that: Currency): Currency = ???
  def * (x: Double): Currency = ???
