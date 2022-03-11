package com.peknight.demo.fpinscala.parsing

trait JSON

object JSON {
  case object JNull extends JSON
  case class JNumber(get: Double) extends JSON
  case class JString(get: String) extends JSON
  case class JBool(get: Boolean) extends JSON
  case class JArray(get: IndexedSeq[JSON]) extends JSON
  case class JObject(get: Map[String, JSON]) extends JSON

  def jsonParser[Err, Parser[+_]](P: Parsers[Err, Parser]): Parser[JSON] = {
    // Gives access to all the combinators
    import P._
    val spaces = char(' ').many.slice
    def number: Parser[JNumber] = regex("[0-9]*+(?:.[0-9]+)?".r).map(d => JNumber(d.toDouble))
    def bool: Parser[JBool] = (string("true") or string("false")).map(b => JBool(b.toBoolean))
    ???
  }



}
