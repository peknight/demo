package com.peknight.demo.scala3.typeclasses

trait JsonSerializer[T]:
  def serialize(o: T): String

  extension (a: T)
    def toJson: String = serialize(a)

object JsonSerializer:
  given stringSerializer: JsonSerializer[String] with
    def serialize(s: String) = s"\"$s\""

  given intSerializer: JsonSerializer[Int] with
    def serialize(n: Int) = n.toString

  given longSerializer: JsonSerializer[Long] with
    def serialize(n: Long) = n.toString

  given booleanSerializer: JsonSerializer[Boolean] with
    def serialize(b: Boolean) = b.toString

  given listSerializer[T: JsonSerializer]: JsonSerializer[List[T]] with
    def serialize(ts: List[T]) = s"[${ts.map(_.toJson).mkString(", ")}]"
