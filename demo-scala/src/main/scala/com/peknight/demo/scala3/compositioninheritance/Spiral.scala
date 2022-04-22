package com.peknight.demo.scala3.compositioninheritance

import com.peknight.demo.scala3.compositioninheritance.Element.elem

object Spiral extends App:
  val space = elem(" ")
  val corner = elem("+")

  def spiral(nEdges: Int, direction: Int): Element =
    if nEdges == 1 then elem("+")
    else
      val sp = spiral(nEdges - 1, (direction + 3) % 4)
      def verticalBar = elem('|', 1, sp.height)
      def horizontalBar = elem('-', sp.width, 1)
      if direction == 0 then
        (corner beside horizontalBar) above (sp beside space)
      else if direction == 1 then
        (sp above space) beside (corner above verticalBar)
      else if direction == 2 then
        (space beside sp) above (horizontalBar beside corner)
      else
        (verticalBar above corner) beside (space above sp)

  println(spiral(6, 0))
  println(spiral(11, 0))
  println(spiral(17, 0))

