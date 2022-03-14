package com.peknight.demo.fpinscala.parsing

trait JSON

object JSON {
  case object JNull extends JSON
  case class JNumber(get: Double) extends JSON
  case class JString(get: String) extends JSON
  case class JBool(get: Boolean) extends JSON
  case class JArray(get: IndexedSeq[JSON]) extends JSON
  case class JObject(get: Map[String, JSON]) extends JSON

  def jsonParser[Parser[+_]](P: Parsers[Parser]): Parser[JSON] = {
    // We'll hide the string implicit conversion and promote strings to tokens instead
    // this is a bit nicer than having to write token everywhere
    // Gives access to all the combinators
    import P.{string => _, _}

    import scala.language.implicitConversions
    implicit def tok(s: String) = token(P.string(s))

    def array = surround("[", "]")(
      value.sep(",").map(vs => JArray(vs.toIndexedSeq))
    ).scope("array")

    def obj = surround("{", "}")(
      keyval.sep(",").map(kvs => JObject(kvs.toMap))
    ).scope("object")

    def keyval = escapedQuoted ** (":" *> value)

    def lit = scope("literal") {
      "null".as(JNull) |
      double.map(JNumber) |
      escapedQuoted.map(JString) |
      "true".as(JBool(true)) |
      "false".as(JBool(false))
    }

    def value: Parser[JSON] = lit | obj | array
    root(whitespace *> (obj | array))
  }



}
