package com.peknight.demo.shapeless.labelledgeneric

import com.peknight.demo.shapeless.adtandgeneric.Shape
import com.peknight.demo.shapeless.adtandgeneric.Shape.Circle
import com.peknight.demo.shapeless.introduction.IceCream
import com.peknight.demo.shapeless.labelledgeneric.JsonValue._
import com.peknight.demo.shapeless.labelledgeneric.JsonObjectEncoder._
import shapeless.LabelledGeneric

object DerivingInstancesWithLabelledGenericApp extends App {
  val iceCream = IceCream("Sundae", 1, false)
  val iceCreamJson: JsonValue = JsonObject(List(
    "name" -> JsonString("Sundae"),
    "numCherries" -> JsonNumber(1),
    "inCone" -> JsonBoolean(false)
  ))
  val gen = LabelledGeneric[IceCream].to(iceCream)
  println(gen)
  println(JsonEncoder[IceCream].encode(iceCream))

  println(LabelledGeneric[Shape].to(Circle(1.0)))

  val shape: Shape = Circle(1.0)
  println(JsonEncoder[Shape].encode(shape))
}
