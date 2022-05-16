package com.peknight.demo.scala.typeclasses

case class Address(street: String, city: String, state: String, zip: Int)
object Address:
  given addressSerializer: JsonSerializer[Address] with
    def serialize(a: Address): String =
      import ToJsonMethods.toJson as asJson
      s"""
        |{
        |  "street": ${a.street.asJson},
        |  "city": ${a.city.asJson},
        |  "state": ${a.state.asJson},
        |  "zip": ${a.zip.asJson}
        |}
       """.trim.stripMargin