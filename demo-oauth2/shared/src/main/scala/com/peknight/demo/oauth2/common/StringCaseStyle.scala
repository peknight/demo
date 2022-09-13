package com.peknight.demo.oauth2.common

object StringCaseStyle:
  def camelToSnake(value: String): String =
    value.split("(?<![0-9])(?=[0-9])|(?=\\p{Lu})").filter(_.nonEmpty).map(_.toLowerCase).mkString("_")

  def snakeToCamel(value: String): String =
    value.split("_").filter(_.nonEmpty).toList match
      case Nil => ""
      case head :: tail =>
        s"${head.toLowerCase}${tail.map(word => s"${word.head.toUpper}${word.tail.toLowerCase.mkString}").mkString}"
  end snakeToCamel
