package com.peknight.demo.circe

import cats.syntax.functor.*
import com.peknight.demo.circe.AlgebraicDataType.GenericDerivation
import com.peknight.demo.circe.AlgebraicDataType.GenericDerivation.given
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import io.circe.{Codec, Decoder, Encoder}

object AlgebraicDataType extends App:

  sealed trait Event

  case class Foo(i: Int) extends Event
  case class Bar(s: String) extends Event
  case class Baz(c: Char) extends Event
  case class Qux(values: List[String]) extends Event

  object GenericDerivation:
    given encodeEvent: Encoder[Event] = Encoder.instance {
      case foo @ Foo(_) => foo.asJson
      case bar @ Bar(_) => bar.asJson
      case baz @ Baz(_) => baz.asJson
      case qux @ Qux(_) => qux.asJson
    }

    given decodeEvent: Decoder[Event] =
      List[Decoder[Event]](
        Decoder[Foo].widen,
        Decoder[Bar].widen,
        Decoder[Baz].widen,
        Decoder[Qux].widen
      ).reduceLeft(_ or _)

  end GenericDerivation

  println(decode[Event]("""{ "i": 1000 }"""))
  // res0: Either[io.circe.Error, Event] = Right(Foo(1000))

  println((Foo(100): Event).asJson.noSpaces)
  // res1: String = "{\"i\":100}"

  println(decode[Event]("""{ "i": 1000, "what_am_i": "Foo" }"""))
  // res5: Either[io.circe.Error, Event] = Right(Foo(1000))

end AlgebraicDataType
