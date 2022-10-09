package com.peknight.demo.shapeless.labelledgeneric

import com.peknight.demo.shapeless.adtandgeneric.Shape
import com.peknight.demo.shapeless.adtandgeneric.Shape.*
import com.peknight.demo.shapeless.introduction.IceCream
import com.peknight.demo.shapeless.labelledgeneric.JsonValue.*
import shapeless3.deriving.Labelling

object DerivingInstancesWithLabelledGenericApp extends App:
  val iceCream = IceCream("Sundae", 1, false)
  val iceCreamJson: JsonValue = JsonObject(List(
    "name" -> JsonString("Sundae"),
    "numCherries" -> JsonNumber(1),
    "inCone" -> JsonBoolean(false)
  ))
  println(Labelling[IceCream])

  given JsonEncoder[IceCream] = JsonObjectEncoder.derived
  given JsonEncoder[Rectangle] = JsonObjectEncoder.derived
  given JsonEncoder[Circle] = JsonObjectEncoder.derived
  given JsonEncoder[Shape] = JsonObjectEncoder.derived
  println(JsonEncoder[IceCream].encode(iceCream))
  val shape: Shape = Circle(1.0)
  println(JsonEncoder[Shape].encode(shape))


