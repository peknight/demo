package com.peknight.demo.scala.typeclasses

import com.peknight.demo.scala.typeclasses.ToJsonMethods.*

object JsonApp extends App:
  val json =
    """
      |{
      |  "style": "tennis",
      |  "size": 10,
      |  "inStock": true,
      |  "colors": ["beige", "white", "blue"],
      |  "humor": null
      |}
    """.trim.stripMargin
  println(json)
  println("tennis".toJson)
  println(10.toJson)
  println(true.toJson)
  println(List(1, 2, 3).toJson)

  val addressBook =
    AddressBook(
      List(
        Contact(
          "Bob Smith",
          List(
            Address(
              "12345 Main Street",
              "San Francisco",
              "CA",
              94105
            ),
            Address(
              "500 State Street",
              "Los Angeles",
              "CA",
              90007
            )
          ),
          List(
            Phone(
              1,
              5558881234
            ),
            Phone(
              49,
              5558413323
            )
          )
        )
      )
    )

  println(addressBook.toJson)

