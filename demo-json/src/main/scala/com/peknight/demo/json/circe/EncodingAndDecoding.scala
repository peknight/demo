package com.peknight.demo.json.circe

import io.circe.syntax._
import io.circe.parser.decode


object EncodingAndDecoding extends App {

  val intsJson = List(1, 2, 3).asJson
  // intsJson: io.circe.Json = JArray(
  //   Vector(JNumber(JsonLong(1L)), JNumber(JsonLong(2L)), JNumber(JsonLong(3L)))
  // )
  println(intsJson)

  println(intsJson.as[List[Int]])
  println(decode[List[Int]](intsJson.noSpaces))
  // res0: io.circe.Decoder.Result[List[Int]] = Right(List(1, 2, 3))

}
