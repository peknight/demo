package com.peknight.demo.scalatest.matcher

import org.scalatest.Entry
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class JavaCollectionsAndMapsFunSuite extends AnyFunSuite:
  test("whether a Java Collection or Map is empty") {
    val javaCollection = new java.util.ArrayList[Int]()
    val javaMap = new java.util.HashMap[Int, Int]()
    javaCollection should be (Symbol("empty"))
    javaMap should be (Symbol("empty"))
  }

  test("check the length of a Java List") {
    val javaList = new java.util.ArrayList[Int]()
    for i <- 1 to 9 do javaList.add(i)
    javaList should have length 9
  }

  test("check the size of any Collection or Map") {
    val javaMap: java.util.Map[Int, Int] = new java.util.HashMap[Int, Int]()
    for i <- 1 to 20 do javaMap.put(i, i)
    javaMap should have size 20
    val javaSet = new java.util.HashSet[Int]()
    for i <- 1 to 90 do javaSet.add(i)
    javaSet should have size 90
  }

  test("whether a Java Collection contains a particular element") {
    val javaCollection = new java.util.ArrayList[String]()
    javaCollection.add("five")
    javaCollection should contain ("five")
  }

  test("treat a Java Map as a collection of Entry") {
    val javaMap: java.util.Map[Int, Int] = new java.util.HashMap[Int, Int]()
    javaMap.put(2, 3)
    javaMap should contain (Entry(2, 3))
    javaMap should contain oneOf (Entry(2, 3), Entry(3, 4))
  }

  test("whether a Java Map contains a particular key, or value") {
    val javaMap: java.util.Map[Int, String] = new java.util.HashMap[Int, String]()
    javaMap.put(1, "Howdy")
    javaMap should contain key 1
    javaMap should contain value "Howdy"
  }

