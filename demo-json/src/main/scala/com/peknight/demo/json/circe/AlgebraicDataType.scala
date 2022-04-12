package com.peknight.demo.json.circe

object AlgebraicDataType extends App {
  sealed trait Event

  case class Foo(i: Int) extends Event
  case class Bar(s: String) extends Event
  case class Baz(c: Char) extends Event
  case class Qux(values: List[String]) extends Event


  import cats.syntax.functor._
  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.{Decoder, Encoder}

  object GenericDerivation {
    implicit val encodeEvent: Encoder[Event] = Encoder.instance {
      case foo @ Foo(_) => foo.asJson
      case bar @ Bar(_) => bar.asJson
      case baz @ Baz(_) => baz.asJson
      case qux @ Qux(_) => qux.asJson
    }

    implicit val decodeEvent: Decoder[Event] =
      List[Decoder[Event]](
        Decoder[Foo].widen,
        Decoder[Bar].widen,
        Decoder[Baz].widen,
        Decoder[Qux].widen
      ).reduceLeft(_ or _)
  }


  // 把这行注释掉会发现后面的ShapesDerivation是无效的
  import GenericDerivation._
  import io.circe.parser.decode

  println(decode[Event]("""{ "i": 1000 }"""))
  // res0: Either[io.circe.Error, Event] = Right(Foo(1000))

  println((Foo(100): Event).asJson.noSpaces)
  // res1: String = "{\"i\":100}"


  // To suppress previously imported inplicit codecs. 这一步看起来并没有用
  import GenericDerivation.{decodeEvent => _, encodeEvent => _}

  object ShapesDerivation {
    // 没啥用的引入
//    import io.circe.shapes
    import shapeless.{Coproduct, Generic}

    implicit def encodeAdtNoDiscr[A, Repr <: Coproduct](implicit
                                                        gen: Generic.Aux[A, Repr],
                                                        encodeRepr: Encoder[Repr]
                                                       ): Encoder[A] = encodeRepr.contramap(gen.to)

    implicit def decodeAdtNoDiscr[A, Repr <: Coproduct](implicit
                                                        gen: Generic.Aux[A, Repr],
                                                        decodeRepr: Decoder[Repr]
                                                       ): Decoder[A] = decodeRepr.map(gen.from)

  }


  // 也是没啥用的引入
//  import ShapesDerivation._
//  import io.circe.parser.decode, io.circe.syntax._

  println(decode[Event]("""{ "i": 1000 }"""))
  // res2: Either[io.circe.Error, Event] = Right(Foo(1000))
  // 实际是GenericDerivation在起作用，把`import GenericDerivation._`注释掉会得到Left(DecodingFailure(CNil, List()))

  println((Foo(100): Event).asJson.noSpaces)
  // res3: String = "{\"i\":100}"


  // 看circe文档的意思下面的的内容应该不是最终版，所以先别用了

//  import io.circe.generic.extras.auto._
  import io.circe.generic.extras.Configuration

  implicit val genDevConfig: Configuration =
    Configuration.default.withDiscriminator("what_am_i")

//  import io.circe.parser.decode, io.circe.syntax._

  println((Foo(100): Event).asJson.noSpaces)
  // res4: String = "{\"i\":100}"

  println(decode[Event]("""{ "i": 1000, "what_am_i": "Foo" }"""))
  // res5: Either[io.circe.Error, Event] = Right(Foo(1000))


}
