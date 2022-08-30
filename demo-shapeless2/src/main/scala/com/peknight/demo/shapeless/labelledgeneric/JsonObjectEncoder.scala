package com.peknight.demo.shapeless.labelledgeneric

import com.peknight.demo.shapeless.labelledgeneric.JsonValue.JsonObject
import shapeless.labelled.FieldType
import shapeless.{:+:, ::, CNil, Coproduct, HList, HNil, Inl, Inr, LabelledGeneric, Lazy, Witness}

trait JsonObjectEncoder[A] extends JsonEncoder[A] {
  def encode(value: A): JsonObject
}
object JsonObjectEncoder {
  def createObjectEncoder[A](fn: A => JsonObject): JsonObjectEncoder[A] = fn(_)

  implicit val hnilEncoder: JsonObjectEncoder[HNil] = createObjectEncoder(_ => JsonObject(Nil))

  implicit def hlistObjectEncoder[K <: Symbol, H, T <: HList](implicit witness: Witness.Aux[K],
                                                              hEncoder: Lazy[JsonEncoder[H]],
                                                              tEncoder: JsonObjectEncoder[T])
  : JsonObjectEncoder[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name
    createObjectEncoder { hlist =>
      val head = hEncoder.value.encode(hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      JsonObject((fieldName, head) :: tail.field)
    }
  }

  implicit val cnilObjectEncoder: JsonObjectEncoder[CNil] =
    createObjectEncoder(_ => throw new Exception("Inconceivable!"))

  implicit def coproductObjectEncoder[K <: Symbol, H, T <: Coproduct](implicit witness: Witness.Aux[K],
                                                                      hEncoder: Lazy[JsonEncoder[H]],
                                                                      tEncoder: JsonObjectEncoder[T])
  : JsonObjectEncoder[FieldType[K, H] :+: T] = {
    val typeName = witness.value.name
    createObjectEncoder {
      case Inl(h) => JsonObject(List(typeName -> hEncoder.value.encode(h)))
      case Inr(t) => tEncoder.encode(t)
    }
  }

  implicit def genericObjectEncoder[A, H](implicit generic: LabelledGeneric.Aux[A, H],
                                          hEncoder: Lazy[JsonObjectEncoder[H]]): JsonEncoder[A] =
    createObjectEncoder(value => hEncoder.value.encode(generic.to(value)))

}
