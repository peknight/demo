package com.peknight.demo.scala3.typeclasses

case class Contact(name: String, addresses: List[Address], phones: List[Phone])
object Contact:
  given contactSerializer: JsonSerializer[Contact] with
    def serialize(c: Contact): String =
      import ToJsonMethods.toJson as asJson
      s"""
        |{
        |  "name": ${c.name.asJson},
        |  "addresses": ${c.addresses.asJson},
        |  "phones": ${c.phones.asJson}
        |}
       """.trim.stripMargin