package com.peknight.demo.scala.extensionmethods

import com.peknight.demo.scala.extensionmethods.TwosComplementOps.*

object ExtensionMethodsApp extends App:
  extension (s: String)
    def singleSpace: String =
      s.trim.split("\\s+").mkString(" ")
//  def singleSpace(s: String): String = s.trim.split("\\s+").mkString(" ")

  println("A Tale\tof Two  Cities".singleSpace)
  println("  It was  the\t\tbest\nof times. ".singleSpace)
  val s = "One  Fish, Two\tFish "
  val t = " One Fish,  Two Fish"
  println(s.singleSpace == t.singleSpace)

  extension [T](xs: List[T])
    def tailOption: Option[List[T]] =
      if xs.nonEmpty then Some(xs.tail) else None

  println(List(1, 2, 3).tailOption)
  println(List.empty[Int].tailOption)
  println(List("a", "b", "c").tailOption)
  println(List.empty[String].tailOption)

  // 可以直接这样用
  println(tailOption[Int](List(1, 2, 3)))

  // 溢出
  println(Int.MinValue.abs)

  println(42.absOption)
  println(-42.absOption)
  println(Int.MaxValue.absOption)
  println(Int.MinValue.absOption)

  println(Byte.MaxValue.negateOption)
  println(Byte.MinValue.negateOption)
  println(Long.MaxValue.negateOption)
  println(Long.MinValue.negateOption)





