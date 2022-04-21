package com.peknight.demo.circe

import io.circe.{Decoder, Encoder}

object ShapeDerivation {
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
