package com.peknight.demo.fpinscala.datastructures

sealed trait Tree[+A]

object Tree {
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  def leaf[A](a: A): Tree[A] = Leaf(a)
  def branch[A](l: Tree[A], r: Tree[A]): Tree[A] = Branch(l, r)

  // Exercise 3.25
  def size[A](tree: Tree[A]): Int = tree match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)
  }

  // Exercise 3.26
  def maximum(tree: Tree[Int]): Int = tree match {
    case Leaf(v) => v
    case Branch(l, r) => maximum(l) max maximum(r)
  }

  // Exercise 3.27
  def depth[A](tree: Tree[A]): Int = tree match {
    case Leaf(_) => 0
    case Branch(l, r) => (depth(l) max depth(r)) + 1
  }

  // Exercise 3.28
  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = tree match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  // Exercise 3.29
  def fold[A, B](tree: Tree[A])(f: A => B)(combine: (B, B) => B): B = tree match {
    case Leaf(v) => f(v)
    case Branch(l, r) => combine(fold(l)(f)(combine), fold(r)(f)(combine))
  }

  def sizeViaFold[A](tree: Tree[A]): Int = fold(tree)(_ => 1)(_ + _ + 1)

  def maximumViaFold(tree: Tree[Int]): Int = fold(tree)(identity)(_ max _)

  def depthViaFold[A](tree: Tree[A]): Int = fold(tree)(_ => 0)((lDepth, rDepth) => (lDepth max rDepth) + 1)

  def mapViaFold[A, B](tree: Tree[A])(f: A => B): Tree[B] = fold(tree)(v => leaf(f(v)))(branch)
}
