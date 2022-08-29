package com.peknight.demo.shapeless.typesandimplicits

object DependentlyTypedFunctionsApp extends App:
  // Experimental
  // println(("foo" *: 123 *: EmptyTuple).last)
  // println((321 *: "bar" *: EmptyTuple).last)

  val last1 = Last[String *: Int *: EmptyTuple]
  val last2 = Last[Int *: String *: EmptyTuple]
  println(last1("foo" *: 123 *: EmptyTuple))
  println(last2(321 *: "bar" *: EmptyTuple))

  val second1 = Second[String *: Boolean *: Int *: EmptyTuple]
  val second2 = Second[String *: Int *: Boolean *: EmptyTuple]
  // Second[String *: EmptyTuple]
  println(second1("foo" *: true *: 123 *: EmptyTuple))
  println(second2("bar" *: 321 *: false *: EmptyTuple))
  // second1("baz" *: EmptyTuple)


