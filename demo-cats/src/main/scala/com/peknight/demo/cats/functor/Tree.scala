package com.peknight.demo.cats.functor

import scala.annotation.tailrec

sealed trait Tree[+A]

object Tree:
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
  def leaf[A](value: A): Tree[A] = Leaf(value)

  def flatten[A](tree: Tree[A]): List[Option[A]] =
    @tailrec def loop(open: List[Tree[A]], closed: List[Option[A]]): List[Option[A]] = open match
      case Branch(left, right) :: tail => loop(left :: right :: tail, None :: closed)
      case Leaf(elem) :: tail => loop(tail, Some(elem) :: closed)
      case Nil => closed
    loop(List(tree), Nil)

  def rebuild[A](list: List[Option[A]]): Tree[A] =
    list.foldLeft(List.empty[Tree[A]]) { (acc: List[Tree[A]], maybe: Option[A]) =>
      maybe.map(Leaf(_) :: acc).getOrElse {
        val left :: right :: tail = acc: @unchecked
        Branch(left, right) :: tail
      }
    }.head