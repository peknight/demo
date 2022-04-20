package com.peknight.demo.cats.introduction

object JsonSyntax:
  extension [A](value: A)
    def toJson(using w: JsonWriter[A]): Json = w.write(value)