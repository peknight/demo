package com.peknight.demo.scala.traits

class Frog extends Animal, Philosophical("I croak, therefore I am!"), HasLegs:
  override def toString: String = "green"
