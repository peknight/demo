package com.peknight.demo.shapeless2.labelledgeneric

import com.peknight.demo.shapeless2.labelledgeneric.JsonValue._

trait JsonEncoder[A] {
  def encode(value: A): JsonValue
}
object JsonEncoder {
  def apply[A](implicit enc: JsonEncoder[A]): JsonEncoder[A] = enc

  def createEncoder[A](func: A => JsonValue): JsonEncoder[A] = func(_)

  implicit val stringEncoder: JsonEncoder[String] = createEncoder(JsonString)

  implicit val doubleEncoder: JsonEncoder[Double] = createEncoder(JsonNumber)

  implicit val intEncoder: JsonEncoder[Int] = createEncoder(value => JsonNumber(value))

  implicit val booleanEncoder: JsonEncoder[Boolean] = createEncoder(JsonBoolean)

  implicit def listEncoder[A](implicit enc: JsonEncoder[A]): JsonEncoder[List[A]] =
    createEncoder(list => JsonArray(list.map(enc.encode)))

  implicit def optionEncoder[A](implicit enc: JsonEncoder[A]): JsonEncoder[Option[A]] =
    createEncoder(opt => opt.map(enc.encode).getOrElse(JsonNull))
}
