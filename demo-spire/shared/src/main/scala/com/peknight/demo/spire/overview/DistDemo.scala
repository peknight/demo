package com.peknight.demo.spire.overview

import spire.*
import spire.implicits.*
import spire.math.*
import spire.random.Dist

object DistDemo extends App:
  val rng = spire.random.rng.Cmwc5()
  given nextMap: Dist[Map[Int, Complex[Double]]] = Dist.map[Int, Complex[Double]](10, 20)
  val m = rng.next[Map[Int, Complex[Double]]]
  val n = rng.next[Double]
  val q = rng.next[Complex[Double]]
  println(s"nextMap = $nextMap, m = $m, n = $n, q = $q")
