package com.peknight.demo.shapeless.labelledgeneric

import com.peknight.demo.shapeless.labelledgeneric.JsonValue.JsonObject
import scala.deriving.Mirror
import shapeless3.deriving.{Continue, K0, Labelling}

trait JsonObjectEncoder[A] extends JsonEncoder[A]:
  def encode(value: A): JsonObject

object JsonObjectEncoder:
  def createObjectEncoder[A](fn: A => JsonObject): JsonObjectEncoder[A] = fn(_)

  given JsonObjectEncoder[EmptyTuple] with
    def encode(value: EmptyTuple): JsonObject = JsonObject(Nil)

  given jsonObjectEncoderSum[A](using inst: => K0.CoproductInstances[JsonEncoder, A], labelling: Labelling[A],
                                mirror: Mirror.SumOf[A]): JsonObjectEncoder[A] with
    def encode(value: A): JsonObject = JsonObject(List(
      labelling.elemLabels(mirror.ordinal(value)) -> inst.ordinal(value).asInstanceOf[JsonEncoder[A]].encode(value)
    ))

  given jsonObjectEncoderProduct[A](using inst: => K0.ProductInstances[JsonEncoder, A], labelling: Labelling[A])
  : JsonObjectEncoder[A] with
    def encode(value: A): JsonObject = JsonObject(labelling.elemLabels.zip(inst.foldRight[List[JsonValue]](value)(Nil)(
      [T] => (jsonEncoder: JsonEncoder[T], t: T, acc: List[JsonValue]) =>
        Continue(jsonEncoder.encode(t) :: acc)
    )).toList)

  inline def derived[A](using gen: K0.Generic[A]): JsonObjectEncoder[A] =
    gen.derive(jsonObjectEncoderProduct, jsonObjectEncoderSum)

