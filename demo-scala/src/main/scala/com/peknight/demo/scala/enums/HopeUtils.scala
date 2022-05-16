package com.peknight.demo.scala.enums

object HopeUtils:

  given hopeOrdering[T](using ord: Ordering[T]): Ordering[Hope[T]] with
    def compare(x: Hope[T], y: Hope[T]): Int =
      import Hope.{Glad, Sad}
      (x, y) match
        case (Sad, Sad) => 0
        case (Sad, _) => -1
        case (_, Sad) => 1
        case (Glad(lhv), Glad(rhv)) => ord.compare(lhv, rhv)
