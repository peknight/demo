package com.peknight.demo.fpinscala.datastructures

import com.peknight.demo.fpinscala.datastructures.Tree.*

object TreeApp extends App:

  val tree = branch(branch(leaf(1), branch(leaf(2), branch(branch(leaf(3), leaf(4)), leaf(5)))), leaf(6))
  println(size(tree))
  println(sizeViaFold(tree))
  println(maximum(tree))
  println(maximumViaFold(tree))
  println(depth(tree))
  println(depthViaFold(tree))
  println(map(tree)(_ * 2))
  println(mapViaFold(tree)(_ * 2))

end TreeApp
