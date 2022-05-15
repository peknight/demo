package com.peknight.demo.js.lihaoyi.upickle.ujson

import ujson.{BytesRenderer, StringRenderer, Value}
import upickle.core.NoOpVisitor

object TransformationsApp:

  def transformations() =
    val exampleAst = ujson.Arr(1, 2, 3)
    println(exampleAst)
    println(ujson.transform("[1, 2, 3]", Value))
    println(ujson.transform(exampleAst, StringRenderer()).toString)
    println(ujson.transform(exampleAst, BytesRenderer()).toByteArray.mkString(" "))
    println(ujson.transform("[1, 2, 3]", StringRenderer()).toString)
    println(ujson.transform("[1, 2, 3]", StringRenderer(indent = 4)).toString)
    println(ujson.transform("[1, 2, 3]".getBytes, StringRenderer()).toString)
    // ujson.transform("[1, 2, 3", NoOpVisitor)
    println(ujson.transform("[1, 2, 3]", upickle.default.reader[Seq[Int]]))
    println(ujson.transform(upickle.default.transform(Seq(1, 2, 3)), StringRenderer()).toString)
