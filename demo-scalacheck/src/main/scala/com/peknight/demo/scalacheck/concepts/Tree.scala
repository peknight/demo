package com.peknight.demo.scalacheck.concepts

sealed abstract class Tree
object Tree:
  case class Node(left: Tree, right: Tree, v: Int) extends Tree
  case object Leaf extends Tree
end Tree