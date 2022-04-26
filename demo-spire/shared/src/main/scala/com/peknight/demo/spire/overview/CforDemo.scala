package com.peknight.demo.spire.overview

import spire.syntax.cfor.*

object CforDemo extends App:
  cfor(0)(_ < 10, _ + 1) { i =>
    println(i)
  }

  def selectionSort(ns: Array[Int]) =
    val limit = ns.length - 1
    cfor(0)(_ < limit, _ + 1) { i =>
      var k = i
      val n = ns(i)
      cfor(i + 1)(_ <= limit, _ + 1) { j =>
        if ns(j) < ns(k) then k = j
      }
      ns(i) = ns(k)
      ns(k) = n
    }

