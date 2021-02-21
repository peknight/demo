package com.peknight.demo.json.circe

import cats.syntax.either._
import io.circe._
import io.circe.parser._

object TraversingAndModifying extends App {

  val json: String = """
  {
    "id": "c730433b-082c-4984-9d66-855c243266f0",
    "name": "Foo",
    "counts": [1, 2, 3],
    "values": {
      "bar": true,
      "baz": 100.001,
      "qux": ["a", "b"]
    }
  }
"""

  val doc: Json = parse(json).getOrElse(Json.Null)

  val cursor: HCursor = doc.hcursor

  val baz: Decoder.Result[Double] =
    cursor.downField("values").downField("baz").as[Double]
  // baz: Decoder.Result[Double] = Right(100.001)
  println(baz)

  // You can also use `get[A](key)` as shorthand for `downField(key).as[A]`
  val baz2: Decoder.Result[Double] =
    cursor.downField("values").get[Double]("baz")
  // baz2: Decoder.Result[Double] = Right(100.001)
  println(baz2)

  val secondQux: Decoder.Result[String] =
    cursor.downField("values").downField("qux").downArray.as[String]
  // secondQux: Decoder.Result[String] = Right("a")
  println(secondQux)

  val reversedNameCursor: ACursor =
    cursor.downField("name").withFocus(_.mapString(_.reverse))

  val reversedName: Option[Json] = reversedNameCursor.top

  println(reversedName)

}
