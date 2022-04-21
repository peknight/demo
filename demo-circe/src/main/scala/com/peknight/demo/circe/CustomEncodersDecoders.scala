package com.peknight.demo.circe

import io.circe._
import io.circe.generic.extras._
import io.circe.parser.decode
import io.circe.syntax._

import java.time.Instant
import scala.util.Try

object CustomEncodersDecoders extends App {

  class Thing(val foo: String, val bar: Int)

  implicit val encodeFoo: Encoder[Thing] = new Encoder[Thing] {
    final def apply(a: Thing): Json = Json.obj(
      ("foo", Json.fromString(a.foo)),
      ("bar", Json.fromInt(a.bar))
    )
  }
  // encodeFoo: Encoder[Thing] = repl.Session$App$$anon$1@40838c90

  implicit val decodeFoo: Decoder[Thing] = new Decoder[Thing] {
    final def apply(c: HCursor): Decoder.Result[Thing] =
      for {
        foo <- c.downField("foo").as[String]
        bar <- c.downField("bar").as[Int]
      } yield {
        new Thing(foo, bar)
      }
  }
  // decodeFoo: Decoder[Thing] = repl.Session$App$$anon$2@682be297

  implicit val encodeInstant: Encoder[Instant] = Encoder.encodeString.contramap[Instant](_.toString)
  // encodeInstant: Encoder[Instant] = io.circe.Encoder$$anon$1@2a71e163

  implicit val decodeInstant: Decoder[Instant] = Decoder.decodeString.emapTry { str =>
    Try(Instant.parse(str))
  }
  // decodeInstant: Decoder[Instant] = io.circe.Decoder$$anon$14@4af8788f

  val instantJson = Instant.now().asJson
  println(instantJson)
  println(instantJson.as[Instant])

  case class Foo(value: String)

  implicit val fooKeyEncoder: KeyEncoder[Foo] = new KeyEncoder[Foo] {
    override def apply(foo: Foo): String = foo.value
  }
  // fooKeyEncoder: KeyEncoder[Foo] = repl.Session$App$$anon$3@16438a4c
  val map = Map[Foo, Int](
    Foo("hello") -> 123,
    Foo("world") -> 456
  )
  // map: Map[Foo, Int] = Map(Foo("hello") -> 123, Foo("world") -> 456)

  val json = map.asJson
  println(json)
  // json: Json = JObject(object[hello -> 123,world -> 456])

  implicit val fooKeyDecoder: KeyDecoder[Foo] = new KeyDecoder[Foo] {
    override def apply(key: String): Option[Foo] = Some(Foo(key))
  }
  // fooKeyDecoder: KeyDecoder[Foo] = repl.Session$App$$anon$4@7ea44693

  println(json.as[Map[Foo, Int]])
  // res0: Decoder.Result[Map[Foo, Int]] = Right(
  //   Map(Foo("hello") -> 123, Foo("world") -> 456)
  // )

  {
    implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
    // config: Configuration = Configuration(
    //   io.circe.generic.extras.Configuration$$$Lambda$7101/1793879894@5c19308a,
    //   io.circe.generic.extras.Configuration$$$Lambda$7100/1456820902@63c9a0a3,
    //   false,
    //   None,
    //   false
    // )

    @ConfiguredJsonCodec case class User(firstName: String, lastName: String)

    val userJson = User("Foo", "McBar").asJson
    println(userJson)
    // res1: Json = JObject(object[first_name -> "Foo",last_name -> "McBar"])
    println(decode[User](userJson.noSpaces))
  }

  {
    implicit val config: Configuration = Configuration.default.copy(
      transformMemberNames = {
        case "i" => "my-int"
        case other => other
      }
    )
    // config: Configuration = Configuration(
    //   <function1>,
    //   io.circe.generic.extras.Configuration$$$Lambda$7100/1456820902@63c9a0a3,
    //   false,
    //   None,
    //   false
    // )

    @ConfiguredJsonCodec case class Bar(i: Int, s: String)

    val barJson = Bar(13, "Qux").asJson
    println(barJson)
    // res3: io.circe.Json = JObject(object[my-int -> 13,s -> "Qux"])
    println(decode[Bar](barJson.noSpaces))
  }

  {
    implicit val config: Configuration = Configuration.default
    // config: Configuration = Configuration(
    //   io.circe.generic.extras.Configuration$$$Lambda$7099/2003630340@d446ae3,
    //   io.circe.generic.extras.Configuration$$$Lambda$7100/1456820902@63c9a0a3,
    //   false,
    //   None,
    //   false
    // )

    @ConfiguredJsonCodec case class Bar(@JsonKey("my-int") i: Int, s: String)

    val barJson = Bar(13, "Qux").asJson
    println(barJson)
    // res5: io.circe.Json = JObject(object[my-int -> 13,s -> "Qux"])
    println(decode[Bar](barJson.noSpaces))
  }

  {
    case class User(firstName: String, lastName: String)
    case class Bar(i: Int, s: String)

    implicit val encodeUser: Encoder[User] =
      Encoder.forProduct2("first_name", "last_name")(u => (u.firstName, u.lastName))
    // encodeUser: Encoder[User] = io.circe.ProductEncoders$$anon$2@4f4f42a6

    implicit val decodeUser: Decoder[User] =
      Decoder.forProduct2("first_name", "last_name")(User.apply)

    implicit val encodeBar: Encoder[Bar] =
      Encoder.forProduct2("my-int", "s")(b => (b.i, b.s))
    // encodeBar: Encoder[Bar] = io.circe.ProductEncoders$$anon$2@33ee5d85

    implicit val decodeBar: Decoder[Bar] =
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
  }


}
