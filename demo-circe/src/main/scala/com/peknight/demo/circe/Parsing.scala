package com.peknight.demo.circe

import io.circe.*
import io.circe.parser.*

object Parsing extends App:

  val rawJson: String =
    """
      |{
      |  "foo": "bar",
      |  "baz": 123,
      |  "list of stuff": [ 4, 5, 6 ]
      |}
    """.trim.stripMargin

  val parseResult = parse(rawJson)
  // parseResult: Either[ParsingFailure, Json] =
  //   Right(JObject(object[foo -> "bar",baz -> 123,list of stuff -> [4, 5, 6]]))
  println(parseResult)

  val badJson: String = "yolo"

  println(parse(badJson))
  // res0: Either[ParsingFailure, Json] = Left(
  //   ParsingFailure(
  //     "expected json value got 'yolo' (line 1, column 1)",
  //     ParseException("expected json value got 'yolo' (line 1, column 1)", 0, 1, 1)
  //   )
  // )

  parse(rawJson) match
    case Left(failure) => println("Invalid JSON :(")
    case Right(json) => println("Yay, got some JSON!")

  val json: Json = parse(rawJson).getOrElse(Json.Null)
  println(json)
  // json: Json = JObject(object[foo -> "bar",baz -> 123,list of stuff -> [4, 5, 6]])

end Parsing
