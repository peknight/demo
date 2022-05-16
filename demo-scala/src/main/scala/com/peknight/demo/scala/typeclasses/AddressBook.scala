package com.peknight.demo.scala.typeclasses

case class AddressBook(contacts: List[Contact])
object AddressBook:
  given addressBookSerializer: JsonSerializer[AddressBook] with
    def serialize(a: AddressBook): String =
      import ToJsonMethods.toJson as asJson
      s"""
        |{
        |  "contacts": ${a.contacts.asJson}
        |}
       """.trim.stripMargin
