package com.peknight.demo.js.common.std

sealed trait Color:
  def value: String

object Color:
  case class RGB(red: Int, green: Int, blue: Int) extends Color:
    val value: String = s"rgb($red, $green, $blue)"

  case class Hex(value: String) extends Color

  case object White extends Color:
    val value: String = "white"

  case object Red extends Color:
    val value: String = "red"

  case object Green extends Color:
    val value: String = "green"

  case object Blue extends Color:
    val value: String = "blue"

  case object Black extends Color:
    val value: String = "black"

