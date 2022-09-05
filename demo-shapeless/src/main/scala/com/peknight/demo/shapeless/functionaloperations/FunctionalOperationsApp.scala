package com.peknight.demo.shapeless.functionaloperations

import scala.Tuple.FlatMap
import scala.runtime.Tuples.fromIArray
import com.peknight.demo.shapeless.functionaloperations.ProductMapper.*

object FunctionalOperationsApp extends App:
  println(myPoly.apply(123))
  println(myPoly.apply("hello"))
  val a = myPoly.apply(123)
  val b: Double = a
  val c: Double = myPoly.apply(123)
  val d: Double = myPoly.apply[Int](123)

  type SizeOf[_] = Int
  println((10 *: "hello" *: true *: EmptyTuple).map[SizeOf]{ [A] => (a: A) => a match
    case i: Int => i
    case str: String => str.length
    case bool: Boolean => if bool then 1 else 0
  })

  extension [Tup <: Tuple] (tup: Tup)
    def flatMap[F[_] <: Tuple](f: [t] => t => F[t]): FlatMap[tup.type, F] = {
      tup match
        case empty: EmptyTuple => empty
        case _ => fromIArray(tup.productIterator.flatMap(f(_).productIterator.map(_.asInstanceOf[Any]))
          .toArray.asInstanceOf[IArray[Object]])
    }.asInstanceOf[FlatMap[tup.type, F]]
    def foldLeft[Z](z: Z)(f: [t] => (Z, t) => Z): Z = tup.productIterator.foldLeft[Z](z) { (z, t) =>
      f(z, t)
    }
  end extension

  type ValueAndSizeOf[A] = A *: Int *: EmptyTuple
  println((10 *: "hello" *: true *: EmptyTuple).flatMap[ValueAndSizeOf] { [A] => (a: A) => a match
    case i: Int => i *: i *: EmptyTuple
    case str: String => str *: str.length *: EmptyTuple
    case bool: Boolean => bool *: (if bool then 1 else 0) *: EmptyTuple
  })

  println((10 *: "hello" *: 100 *: EmptyTuple).foldLeft(0){ [A] => (z: Int, a: A) => a match
    case i: Int => z + i
    case str: String => z + str.length
  })

  type Conversions[A] = A match {
    case Int => Boolean
    case Boolean => Int
    case _ => A
  }

  def conversions[A](a: A): Conversions[A] = a match
    case i: Int => i > 0
    case bool: Boolean => if bool then 1 else 0
    case _ => a

  println(IceCream1("Sundae", 1, false).mapTo[IceCream2]([A] => (a: A) => conversions(a)))

