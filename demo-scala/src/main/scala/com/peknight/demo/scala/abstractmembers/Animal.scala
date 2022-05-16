package com.peknight.demo.scala.abstractmembers

abstract class Animal:
  type SuitableFood <: Food
  def eat(food: SuitableFood): Unit
