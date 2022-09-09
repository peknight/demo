package com.peknight.demo.catsparse.tutorial

object JsonParserApp extends App:

  val jsonTxt =
    """
      |{
      |  "Company name" : "Microsoft Corporation",
      |  "Ticker"  : "MSFT",
      |  "Active"  : true,
      |  "Price"   : 30.66,
      |  "Shares outstanding" : 8.38e9,
      |  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
      |}
    """.trim.stripMargin

  val malformedJson1 =
    """
      |{
      |  "Company name" ; "Microsoft Corporation"
      |}
    """.trim.stripMargin

  val malformedJson2 =
    """
      |[
      |  [ "HPQ", "IBM",
      |  "YHOO", "DELL" ++
      |  "GOOG"
      |  ]
      |]
    """.trim.stripMargin

  val jsonTxt2 =
    """
      |{
      |  "key" : "An \n important \"Quotation\""
      |}
    """.trim.stripMargin

  println(Json.parser.parse(jsonTxt))
  println(Json.parser.parse(malformedJson1))
  println(Json.parser.parse(malformedJson2))
  println(Json.parser.parse(jsonTxt2))

