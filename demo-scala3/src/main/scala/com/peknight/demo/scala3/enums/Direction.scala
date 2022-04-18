package com.peknight.demo.scala3.enums

import com.peknight.demo.scala3.enums.Direction.{East, North, South, West}

/**
 * 可以写作enum Direction extends java.lang.Enum[Direction]: 来扩展处java的枚举类型
 */
enum Direction(val degrees: Int):
  def invert: Direction =
    this match
      case North => South
      case East => West
      case South => North
      case West => East
  case North extends Direction(0)
  case East extends Direction(90)
  case South extends Direction(180)
  case West extends Direction(270)

object Direction:
  def nearestTo(degrees: Int): Direction =
    val rem = degrees % 360
    val angle = if rem < 0 then rem + 360 else rem
    val (ne, se, sw, nw) = (45, 135, 225, 315)
    angle match
      case a if a > nw || a <= ne => North
      case a if a > ne && a <= se => East
      case a if a > se && a <= sw => South
      case a if a > sw && a <= nw => West

  def allButNeartest(degrees: Int): List[Direction] =
    val nearest = Direction.nearestTo(degrees)
    Direction.values.toList.filter(_ != nearest)
