package com.peknight.demo.monocle.optional

import monocle.Optional

object OptionalApp extends App:
  val head = Optional[List[Int], Int] {
    case Nil => None
    case x :: xs => Some(x)
  } { a => {
    case Nil => Nil
    case x :: xs => a :: xs
    }
  }

  val xs = List(1, 2, 3)
  val ys = List.empty[Int]
  // if it matches
  println(head.nonEmpty(xs))
  println(head.nonEmpty(ys))

  // ? 为啥这个方法名带个`modify`？没有修改啊
  head.getOrModify(xs)
  head.getOrModify(ys)

  println(head.getOption(xs))
  println(head.replace(5)(xs))

  println(head.getOption(ys))
  println(head.replace(5)(ys))

  println(head.modify(_ + 1)(xs))
  println(head.modify(_ + 1)(ys))

  // to know if the update was successful:
  println(head.modifyOption(_ + 1)(xs))
  println(head.modifyOption(_ + 1)(ys))

