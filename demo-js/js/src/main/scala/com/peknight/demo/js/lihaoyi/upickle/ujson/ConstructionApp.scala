package com.peknight.demo.js.lihaoyi.upickle.ujson

import com.peknight.demo.js.lihaoyi.upickle.basics.Thing

object ConstructionApp:

  def construction() =
    val json0 = ujson.Arr(
      ujson.Obj("myFieldA" -> ujson.Num(1), "myFieldB" -> ujson.Str("g")),
      ujson.Obj("myFieldA" -> ujson.Num(2), "myFieldB" -> ujson.Str("k"))
    )
    // `ujson.Num`和`ujson.Str`的调用是可选的
    val json1 = ujson.Arr(
      ujson.Obj("myFieldA" -> 1, "myFieldB" -> "g"),
      ujson.Obj("myFieldA" -> 2, "myFieldB" -> "k")
    )
    println(json0)
    println(json1)
    println(json1(0)("myFieldA").num)
    println(json1(0)("myFieldB").str)
    println(json1(1)("myFieldA").num)
    println(json1(1)("myFieldB").str)
    println(ujson.write(json1))

    val str = """[{"myFieldA":1,"myFieldB":"g"},{"myFieldA":2,"myFieldB":"k"}]"""
    val json2: ujson.Value = ujson.read(str)
    println(json2)
    // 是可变对象，差评
    println(json2.arr.remove(1))
    println(json2)
    json2(0)("myFieldA") = 1337
    json2(0)("myFieldB") = json2(0)("myFieldB").str + "lols"
    println(json2)

    val json3: ujson.Value = ujson.read(str)
    json3(0)("myFieldA") = _.num + 100
    json3(1)("myFieldB") = _.str + "lol"
    println(json3)

    val data = Seq(Thing(1, "g"), Thing(2, "k"))
    val json4 = upickle.default.writeJs(data)
    println(json4)
    json4.arr.remove(1)
    json4(0)("myFieldA") = 1337
    println(upickle.default.read[Seq[Thing]](json4))
