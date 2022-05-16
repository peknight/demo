package com.peknight.demo.scala.abstractmembers

class Cow extends Animal:
  type SuitableFood = Grass
  override def eat(food: Grass): Unit = {}
