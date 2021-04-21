package com.peknight.demo.fpinscala.parallelism

object ParallelismApp extends App {
  def sum(ints: Seq[Int]): Int =
    ints.foldLeft(0)((a, b) => a + b)

  def sumViaDivideConquer(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) ints.headOption getOrElse 0 else {
      val (l, r) = ints.splitAt(ints.length / 2)
      sumViaDivideConquer(l) + sumViaDivideConquer(r)
    }
  }

  def sumViaParV1(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1) ints.headOption getOrElse 0 else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val sumL: Par[Int] = Par.unit(sumViaParV1(l))
      val sumR: Par[Int] = Par.unit(sumViaParV1(r))
      Par.get(sumL) + Par.get(sumR)
    }
    

}