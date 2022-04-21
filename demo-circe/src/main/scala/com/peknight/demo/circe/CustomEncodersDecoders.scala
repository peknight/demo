package com.peknight.demo.circe

import io.circe.*
import io.circe.parser.decode
import io.circe.syntax.*

import java.time.Instant
import scala.util.Try

object CustomEncodersDecoders extends App:

  class Thing(val foo: String, val bar: Int)

  given encodeFoo: Encoder[Thing] with
    final def apply(a: Thing): Json = Json.obj(
      ("foo", Json.fromString(a.foo)),
      ("bar", Json.fromInt(a.bar))
    )
  // encodeFoo: Encoder[Thing] = repl.Session$App$$anon$1@40838c90

  given decodeFoo: Decoder[Thing] with
    final def apply(c: HCursor): Decoder.Result[Thing] =
      for
        foo <- c.downField("foo").as[String]
        bar <- c.downField("bar").as[Int]
      yield new Thing(foo, bar)

  // decodeFoo: Decoder[Thing] = repl.Session$App$$anon$2@682be297

  given encodeInstant: Encoder[Instant] = Encoder.encodeString.contramap[Instant](_.toString)
  // encodeInstant: Encoder[Instant] = io.circe.Encoder$$anon$1@2a71e163

  given decodeInstant: Decoder[Instant] = Decoder.decodeString.emapTry { str =>
    Try(Instant.parse(str))
  }
  // decodeInstant: Decoder[Instant] = io.circe.Decoder$$anon$14@4af8788f

  val instantJson = Instant.now().asJson
  println(instantJson)
  println(instantJson.as[Instant])

  case class Foo(value: String)

  given fooKeyEncoder: KeyEncoder[Foo] with
    override def apply(foo: Foo): String = foo.value

  // fooKeyEncoder: KeyEncoder[Foo] = repl.Session$App$$anon$3@16438a4c
  val map = Map[Foo, Int](
    Foo("hello") -> 123,
    Foo("world") -> 456
  )
  // map: Map[Foo, Int] = Map(Foo("hello") -> 123, Foo("world") -> 456)

  val json = map.asJson
  println(json)
  // json: Json = JObject(object[hello -> 123,world -> 456])

  given fooKeyDecoder: KeyDecoder[Foo] with
    override def apply(key: String): Option[Foo] = Some(Foo(key))

  // fooKeyDecoder: KeyDecoder[Foo] = repl.Session$App$$anon$4@7ea44693

  println(json.as[Map[Foo, Int]])
  // res0: Decoder.Result[Map[Foo, Int]] = Right(
  //   Map(Foo("hello") -> 123, Foo("world") -> 456)
  // )

  case class User(firstName: String, lastName: String)
  case class Bar(i: Int, s: String)

  given encodeUser: Encoder[User] =
    Encoder.forProduct2("first_name", "last_name")(u => (u.firstName, u.lastName))
  // encodeUser: Encoder[User] = io.circe.ProductEncoders$$anon$2@4f4f42a6

  given decodeUser: Decoder[User] =
    Decoder.forProduct2("first_name", "last_name")(User.apply)

  given encodeBar: Encoder[Bar] =
    Encoder.forProduct2("my-int", "s")(b => (b.i, b.s))
  // encodeBar: Encoder[Bar] = io.circe.ProductEncoders$$anon$2@33ee5d85

  given decodeBar: Decoder[Bar] =
    Decoder.forProduct2("my-int", "s")(Bar.apply)

  val userJson = User("Foo", "McBar").asJson
  println(userJson)
  // res7: io.circe.Json = JObject(
  //   object[first_name -> "Foo",last_name -> "McBar"]
  // )
  println(decode[User](userJson.noSpaces))

  val barJson = Bar(13, "Qux").asJson
  println(barJson)
  // res8: io.circe.Json = JObject(object[my-int -> 13,s -> "Qux"])
  println(decode[Bar](barJson.noSpaces))

end CustomEncodersDecoders
