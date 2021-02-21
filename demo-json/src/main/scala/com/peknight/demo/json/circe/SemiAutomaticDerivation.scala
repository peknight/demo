package com.peknight.demo.json.circe

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.semiauto._
import io.circe.syntax._

object SemiAutomaticDerivation extends App {

  case class Foo(a: Int, b: String, c: Boolean)

  implicit val fooDecoder: Decoder[Foo] = deriveDecoder[Foo]
  implicit val fooEncoder: Encoder[Foo] = deriveEncoder[Foo]

  println(Foo(1, "2", true).asJson)

  // Or simply:
  // implicit val fooDecoder: Decoder[Foo] = deriveDecoder
  // implicit val fooEncoder: Encoder[Foo] = deriveEncoder


  @JsonCodec case class Bar(i: Int, s: String)

  println(Bar(13, "Qux").asJson)
  // res1: Json = JObject(object[i -> 13,s -> "Qux"])

  case class User(id: Long, firstName: String, lastName: String)

  implicit val decodeUser: Decoder[User] =
    Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  // decodeUser: Decoder[User] = io.circe.ProductDecoders$$anon$3@4fbb4788

  implicit val encodeUser: Encoder[User] =
    Encoder.forProduct3("id", "first_name", "last_name")(u =>
      (u.id, u.firstName, u.lastName)
    )
  // encodeUser: Encoder[User] = io.circe.ProductEncoders$$anon$3@62579f28

  println(User(1, "fn", "ln").asJson)

}
