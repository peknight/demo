package com.peknight.demo.fpinscala.datastructures

import com.peknight.demo.fpinscala.datastructures.List.*

object ListApp extends App:

  val ex1: List[Double] = Nil
  val ex2: List[Int] = Cons(1, Nil)
  val ex3: List[String] = Cons("a", Cons("b", Nil))

  println(ex1)
  println(ex2)
  println(ex3)

  val l = List(1, 2, 3, 4, 5)

  // Exercise 3.1
  val x = l match
    case Cons(x, Cons(4, _)) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y // Bingo! 3
    case Cons(h, t) => h + sum(t) // 被截胡
//    case _ => 101

  println(x)

  println(drop(l, 3))
  println(dropWhile(l)(_ < 4))
  println(length(l))
  println(append(l, List(6, 7, 8)))
  println(appendViaFoldLeft(l, List(6, 7, 8)))
  println(appendViaFoldRight(l, List(6, 7, 8)))
  println(removeAllOddNumber(l))
  println(flatMap(List(1, 2, 3))(i => List(i, i)))
  println(hasSubsequence(List(1, 2, 3, 4, 5), List(2, 3)))
  println(hasSubsequence(List(1, 2, 3, 4, 5), List(2, 4)))
  println(hasSubsequence(List(1, 2, 3, 4, 5), Nil))

end ListApp

