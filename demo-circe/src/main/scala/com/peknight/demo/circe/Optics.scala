package com.peknight.demo.circe

object Optics extends App {
  import io.circe._
  import io.circe.parser._

  val json: Json = parse("""
{
  "order": {
    "customer": {
      "name": "Custy McCustomer",
      "contactDetails": {
        "address": "1 Fake Street, London, England",
        "phone": "0123-456-789"
      }
    },
    "items": [{
      "id": 123,
      "description": "banana",
      "quantity": 1
    }, {
      "id": 456,
      "description": "apple",
      "quantity": 2
    }],
    "total": 123.45
  }
}
""").getOrElse(Json.Null)

  val phoneNumFromCursor: Option[String] = json.hcursor.
    downField("order").
    downField("customer").
    downField("contactDetails").
    get[String]("phone").
    toOption
  // phoneNumFromCursor: Option[String] = Some("0123-456-789")
  println(phoneNumFromCursor)

  import io.circe.optics.JsonPath._

  val _phoneNum = root.order.customer.contactDetails.phone.string
  // _phoneNum: monocle.package.Optional[Json, String] = monocle.POptional$$anon$1@19fc4f16

  val phoneNum: Option[String] = _phoneNum.getOption(json)
  // phoneNum: Option[String] = Some("0123-456-789")

  println(phoneNum)

  val itemsFromCursor: Vector[Json] = json.hcursor.
    downField("order").
    downField("items").
    focus.
    flatMap(_.asArray).
    getOrElse(Vector.empty)
  // itemsFromCursor: Vector[Json] = Vector(
  //   JObject(object[id -> 123,description -> "banana",quantity -> 1]),
  //   JObject(object[id -> 456,description -> "apple",quantity -> 2])
  // )
  println(itemsFromCursor)

  val quantities: Vector[Int] =
    itemsFromCursor.flatMap(_.hcursor.get[Int]("quantity").toOption)
  // quantities: Vector[Int] = Vector(1, 2)
  println(quantities)

  val items: List[Int] =
    root.order.items.each.quantity.int.getAll(json)
  // items: List[Int] = List(1, 2)
  println(items)


  val doubleQuantities: Json => Json =
    root.order.items.each.quantity.int.modify(_ * 2)
  // doubleQuantities: Json => Json = monocle.PTraversal$$Lambda$7443/2102627216@76eee79c

  val modifiedJson = doubleQuantities(json)
  // modifiedJson: Json = JObject(
  //   object[order -> {
  //   "customer" : {
  //     "name" : "Custy McCustomer",
  //     "contactDetails" : {
  //       "address" : "1 Fake Street, London, England",
  //       "phone" : "0123-456-789"
  //     }
  //   },
  //   "items" : [
  //     {
  //       "id" : 123,
  //       "description" : "banana",
  //       "quantity" : 2
  //     },
  //     {
  //       "id" : 456,
  //       "description" : "apple",
  //       "quantity" : 4
  //     }
  //   ],
  //   "total" : 123.45
  // }]
  // )
  println(modifiedJson)


  import io.circe.optics.JsonOptics._
  import monocle.function.Plated

  val transformedJson = Plated.transform[Json] { j =>
    j.asNumber match {
      case Some(n) => Json.fromString(n.toString)
      case None    => j
    }
  }(json)
  println(transformedJson)
  // res0: Json = JObject(
  //   object[order -> {
  //   "customer" : {
  //     "name" : "Custy McCustomer",
  //     "contactDetails" : {
  //       "address" : "1 Fake Street, London, England",
  //       "phone" : "0123-456-789"
  //     }
  //   },
  //   "items" : [
  //     {
  //       "id" : "123",
  //       "description" : "banana",
  //       "quantity" : "1"
  //     },
  //     {
  //       "id" : "456",
  //       "description" : "apple",
  //       "quantity" : "2"
  //     }
  //   ],
  //   "total" : "123.45"
  // }]
  // )





}
