package com.peknight.demo.shapeless.derivation

import com.peknight.demo.shapeless.autoderiving.CsvEncoder

enum Tree[T] derives Eq, EqShapeless, CsvEncoder:
  case Branch(left: Tree[T], right: Tree[T])
  case Leaf(elem: T)


