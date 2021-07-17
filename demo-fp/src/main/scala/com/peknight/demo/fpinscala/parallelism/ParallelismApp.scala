package com.peknight.demo.fpinscala.parallelism

import com.peknight.demo.fpinscala.parallelism.Par._

import java.util.concurrent.Executors

object ParallelismApp extends App {
  def sum(ints: Seq[Int]): Int =
    ints.foldLeft(0)((a, b) => a + b)

  def sumViaDivideConquer(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) ints.headOption getOrElse 0 else {
      val (l, r) = ints.splitAt(ints.length / 2)
      sumViaDivideConquer(l) + sumViaDivideConquer(r)
    }
  }

//  def sumViaParV1(ints: IndexedSeq[Int]): Int =
//    if (ints.size <= 1) ints.headOption getOrElse 0 else {
//      val (l, r) = ints.splitAt(ints.length / 2)
//      val sumL: Par[Int] = Par.unit(sumViaParV1(l))
//      val sumR: Par[Int] = Par.unit(sumViaParV1(r))
//      Par.run(sumL) + Par.run(sumR)
//    }

  def sumViaParV2(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2WithUnitFuture(Par.fork(sumViaParV2(l)), Par.fork(sumViaParV2(r)))(_ + _)
    }

  val S = Executors.newFixedThreadPool(2)

  val echoer = Actor[String](S) {
    msg => println(s"Got message: '$msg' - ${Thread.currentThread().getName()}")
  }

  echoer ! "hello"

  echoer ! "goodbye"

  echoer ! "You're just repeating everything I say, aren't you?"

  val p = Nonblocking.Par.parMap(List.range(1, 100000))(math.sqrt(_))
  val x = Nonblocking.Par.run(S)(p)
  println(x)

  Thread.sleep(100)
  S.shutdown()
}