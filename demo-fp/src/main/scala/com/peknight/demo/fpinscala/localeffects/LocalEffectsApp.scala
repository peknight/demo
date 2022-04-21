package com.peknight.demo.fpinscala.localeffects

import com.peknight.demo.fpinscala.localeffects.ST.STRef

object LocalEffectsApp extends App:

  // 使用Nothing作为类型S
  for
    r1 <- STRef[Nothing, Int](1)
    r2 <- STRef[Nothing, Int](1)
    x <- r1.read
    y <- r2.read
    _ <- r1.write(y + 1)
    _ <- r2.write(x + 1)
    a <- r1.read
    b <- r2.read
  yield (a, b)

  val p = new RunnableST[(Int, Int)]:
    def apply[S] =
      for
        r1 <- STRef(1)
        r2 <- STRef(2)
        x <- r1.read
        y <- r2.read
        _ <- r1.write(y + 1)
        _ <- r2.write(x + 1)
        a <- r1.read
        b <- r2.read
      yield (a, b)

  val r = ST.runST(p)
  println(r)

  println(QuickSort.quickSort(List(123,128,88,123461,34,58,9292)))
