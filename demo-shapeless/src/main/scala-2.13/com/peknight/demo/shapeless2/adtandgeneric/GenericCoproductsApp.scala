package com.peknight.demo.shapeless2.adtandgeneric

import com.peknight.demo.shapeless2.adtandgeneric.Shape.{Circle, Rectangle}
import shapeless.{:+:, CNil, Generic, Inl, Inr}

object GenericCoproductsApp extends App {
  type Light = Red :+: Amber :+: Green :+: CNil
  val red: Light = Inl(Red())
  val amber: Light = Inr(Inl(Amber()))
  val green: Light = Inr(Inr(Inl(Green())))
  val gen = Generic[Shape]
  println(gen.to(Rectangle(3.0, 4.0)))
  println(gen.to(Circle(1.0)))
}
