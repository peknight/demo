package com.peknight.demo.scala3.abstractmembers

/**
 * 改良类型 Demo
 * 黑科技啊
 */
class Pasture:
  var animals: List[Animal { type SuitableFood = Grass }] = Nil
