package com.peknight.demo.circe

import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*

object SemiAutomaticDerivation extends App:

  case class Foo(a: Int, b: String, c: Boolean)

  given fooDecoder: Decoder[Foo] = deriveDecoder[Foo]
  given fooEncoder: Encoder[Foo] = deriveEncoder[Foo]

  println(Foo(1, "2", true).asJson)

  // Or simply:
  // given fooDecoder: Decoder[Foo] = deriveDecoder
  // given fooEncoder: Encoder[Foo] = deriveEncoder

  // import io.circe.generic.JsonCodec
  // @JsonCodec
  case class Bar(i: Int, s: String) derives Codec.AsObject

  println(Bar(13, "Qux").asJson)
  // res1: Json = JObject(object[i -> 13,s -> "Qux"])

  case class User(id: Long, firstName: String, lastName: String)

  given decodeUser: Decoder[User] = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  // decodeUser: Decoder[User] = io.circe.ProductDecoders$$anon$3@4fbb4788

  given encodeUser: Encoder[User] = Encoder.forProduct3("id", "first_name", "last_name")(u =>
    (u.id, u.firstName, u.lastName)
  )
  // encodeUser: Encoder[User] = io.circe.ProductEncoders$$anon$3@62579f28

  println(User(1, "fn", "ln").asJson)

end SemiAutomaticDerivation
