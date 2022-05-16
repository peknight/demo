package com.peknight.demo.scala.tuples

import com.peknight.demo.scala.tuples.BaseEncoders.given
import com.peknight.demo.scala.tuples.Tup.*
import com.peknight.demo.scala.tuples.TupleEncoders.given

import scala.deriving.Mirror

object TuplesApp extends App:

  def tupleToCsv[X <: Tuple : RowEncoder](tuple: X): List[String] =
    summon[RowEncoder[X]].encodeRow(tuple)

  println(tupleToCsv(("Bob", 42, false)))

  val bob: Employee = Employee("Bob", 42, false)
  val bobTuple: (String, Int, Boolean) = Tuple.fromProductTyped(bob)
  println(bob)
  println(bobTuple)
  // Cool
  val bobAgain: Employee = summon[Mirror.Of[Employee]].fromProduct(bobTuple)
  println(bobAgain)

  println((1, 2, 3).size)
  println((3 *: 4 *: 5 *: EmptyTuple).head)
  println((3 *: 4 *: 5 *: EmptyTuple).tail)
  println((3 *: 4 *: 5 *: 6 *: EmptyTuple))
  println((1, 2, 3) ++ (4, 5, 6))
  println((1, 2, 3).drop(2))
  println((1, 2, 3).take(2))
  println((1, 2, 3)(2))
  println((1, 2, 3, 4, 5).splitAt(2))
  println((1, 2, 3).zip(('a', 'b')))
  println((1, 'a', 2).toList)
  println((1, 'a', 2).toArray.mkString(" "))
  println((1, 'a', 2).toIArray.mkString(" "))
  // 好家伙，泛型居然可以这样定义
  println((1, 'a').map[[X] =>> Option[X]]([T] => (t: T) => Some(t)))

  val myTup = TCons(1, TCons(2,  EmpT))
  println(myTup)
  println(1 *: "2" *: EmpT)

  val left = 1 *: 2 *: EmpT
  val right = 3 *: 4 *: EmpT

  println(concat(left, right))



