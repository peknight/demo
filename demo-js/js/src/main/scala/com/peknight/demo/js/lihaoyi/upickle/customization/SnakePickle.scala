package com.peknight.demo.js.lihaoyi.upickle.customization

object SnakePickle extends upickle.AttributeTagged:
  def camelToSnake(s: String) =
    s.replaceAll("([A-Z])", "#$1").split('#').map(_.toLowerCase).mkString("_")

  def snakeToCamel(s: String) =
    val res = s.split("_", -1).map(x => s"${x(0).toUpper}${x.drop(1)}").mkString
    s"${s(0).toLower}${res.drop(1)}"

  override def objectAttributeKeyReadMap(s: CharSequence) = snakeToCamel(s.toString)

  override def objectAttributeKeyWriteMap(s: CharSequence) = camelToSnake(s.toString)

  override def objectTypeKeyReadMap(s: CharSequence) = snakeToCamel(s.toString)

  override def objectTypeKeyWriteMap(s: CharSequence) = camelToSnake(s.toString)

