package com.peknight.demo.scala.marcos.mirror.philippmartini

enum Tree[A] derives PrettyString:
  case Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  case Leaf[A](value: A) extends Tree[A]

object Tree:
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
  def leaf[A](value: A): Tree[A] = Leaf(value)

