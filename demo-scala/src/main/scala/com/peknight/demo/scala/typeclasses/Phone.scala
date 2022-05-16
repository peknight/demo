package com.peknight.demo.scala.typeclasses

case class Phone(countryCode: Int, phoneNumber: Long)
object Phone:
  given phoneSerializer: JsonSerializer[Phone] with
    def serialize(p: Phone): String =
      import ToJsonMethods.toJson as asJson
      s"""
        |{
        |  "countryCode": ${p.countryCode.asJson},
        |  "phoneNumber": ${p.phoneNumber.asJson}
        |}
       """.trim.stripMargin
