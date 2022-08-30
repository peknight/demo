package com.peknight.demo.shapeless.labelledgeneric

import com.peknight.demo.shapeless.labelledgeneric.JsonValue.*

trait JsonEncoder[A]:
  def encode(value: A): JsonValue

object JsonEncoder:
  def apply[A](using enc: JsonEncoder[A]): JsonEncoder[A] = enc

  def createEncoder[A](func: A => JsonValue): JsonEncoder[A] = func(_)

  given JsonEncoder[String] with
    def encode(value: String): JsonValue = JsonString(value)

  given JsonEncoder[Double] with
    def encode(value: Double): JsonValue = JsonNumber(value)

  given JsonEncoder[Int] with
    def encode(value: Int): JsonValue = JsonNumber(value)

  given JsonEncoder[Boolean] with
    def encode(value: Boolean): JsonValue = JsonBoolean(value)

  given [A] (using enc: JsonEncoder[A]): JsonEncoder[List[A]] with
    def encode(value: List[A]): JsonValue = JsonArray(value.map(enc.encode))

  given [A] (using enc: JsonEncoder[A]): JsonEncoder[Option[A]] with
    def encode(value: Option[A]): JsonValue = value.map(enc.encode).getOrElse(JsonNull)
