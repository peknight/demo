package com.peknight.demo.scala3.abstractmembers

abstract class Animal:
  type SuitableFood <: Food
  def eat(food: SuitableFood): Unit
