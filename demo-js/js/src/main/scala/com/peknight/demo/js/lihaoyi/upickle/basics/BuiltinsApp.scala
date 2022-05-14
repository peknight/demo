package com.peknight.demo.js.lihaoyi.upickle.basics

import com.peknight.demo.js.lihaoyi.upickle.basics.IntOrTuple.*
import upickle.*
import upickle.default.*

import scala.collection.immutable.SortedSet

object BuiltinsApp:

  def builtins() =
    println(write(true: Boolean))
    println(write(false: Boolean))

    println(write(12: Int))
    println(write(12: Short))
    println(write(12: Byte))
    println(write(Int.MaxValue))
    println(write(Int.MinValue))
    println(write(12.5f: Float))
    println(write(12.5: Double))

    println(write(12: Long))
    println(write(4000000000000L: Long))
    // 对于js来说Long太大了，超出范围的会序列化为json字符串
    println(write(9223372036854775807L: Long))
    println(write(1.0 / 0: Double))
    println(write(Float.PositiveInfinity))
    println(write(Float.NegativeInfinity))

    // Char也会被写成string
    println(write('o'))
    println(write("omg"))

    // 数组
    println(write(Array(1, 2, 3)))
    // 设置缩进
    println(write(Array(1, 2, 3), indent = 4))

    println(write(Seq(1, 2, 3)))
    println(write(Vector(1, 2, 3)))
    println(write(List(1, 2, 3)))
    println(write(SortedSet(1, 2, 3)))

    // Option会被序列化为拥有0或1个元素的json列表
    println(write(Some(1)))
    println(write(None))

    // 元组会序列化为 heterogenous Json列表
    println(write((1, "omg")))
    println(write((1, "omg", true)))

    println(write(Thing(1, "gg")))
    println(read[Thing]("""{"myFieldA":1,"myFieldB":"gg"}"""))
    println(write(Big(1, true, "lol", 'Z', Thing(7, ""))))
    println(write(Big(1, true, "lol", 'Z', Thing(7, "")), indent = 4))

    println(write(IntThing(1)))
    println(write(TupleThing("naeem", (1, 2))))
    println(read[IntOrTuple]("""{"$type":"com.peknight.demo.js.lihaoyi.upickle.basics.IntOrTuple.IntThing","i":1}"""))

    println(write((((1, 2), (3, 4)), ((5, 6), (7, 8)))))
    println(write(Seq(Thing(1, "g"), Thing(2, "k"))))
    println(write(Bar("bearrr", Seq(Foo(1), Foo(2), Foo(3)))))
    println(write(Bar(null, Seq(Foo(1), null, Foo(3)))))
