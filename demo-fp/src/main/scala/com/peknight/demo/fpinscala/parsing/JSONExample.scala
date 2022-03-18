package com.peknight.demo.fpinscala.parsing

object JSONExample extends App {
  val jsonTxt = """
{
  "Company name" : "Microsoft Corporation",
  "Ticker"  : "MSFT",
  "Active"  : true,
  "Price"   : 30.66,
  "Shares outstanding" : 8.38e9,
  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
}
"""

  val malformedJson1 = """
{
  "Company name" ; "Microsoft Corporation"
}
"""

  val malformedJson2 = """
[
  [ "HPQ", "IBM",
  "YHOO", "DELL" ++
  "GOOG"
  ]
]
"""

  val jsonTxt2 =
    """
{
  "key" : "An \n important \"Quotation\""
}
"""

  val P = Sliceable
  import SliceableType.Parser

  def printResult[E](e: Either[E,JSON]) =
    e.fold(println, println)

  val json: Parser[JSON] = JSON.jsonParser(P)
  printResult { P.run(json)(jsonTxt) }
  println("--")
  printResult { P.run(json)(malformedJson1) }
  println("--")
  printResult { P.run(json)(malformedJson2) }
  println("--")
  printResult { P.run(json)(jsonTxt2) }
}
