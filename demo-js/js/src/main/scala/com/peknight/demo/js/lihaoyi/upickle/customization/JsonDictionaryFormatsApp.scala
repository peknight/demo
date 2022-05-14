package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.*

object JsonDictionaryFormatsApp:

  def jsonDictionaryFormats() =
    println(write(FooId(123)))
    println(read[FooId]("123"))

    // 奇怪的行为：写map最后居然是个列表而不是对象
    // 理由是不是所有的key都能容易的转换为json字符串用作对象的key（可能是指key是对象或者数组结构的情况），所以写成列表方便保持结构
    println(write(Map(FooId(123) -> "hello", FooId(456) -> "world")))
    println(read[Map[FooId, String]]("""[[123, "hello"],[456,"world"]]"""))
    {
      // 可以使用stringKeyRW用于写成json的对象
      given ReadWriter[FooId] = stringKeyRW(readwriter[Int].bimap[FooId](_.x, FooId(_)))
      println(write(Map(FooId(123) -> "hello", FooId(456) -> "world")))
      println(read[Map[FooId, String]]("""{"123":"hello","456":"world"}"""))
    }
    println(write(Map(Seq(1) -> 1, Seq(1, 2) -> 3, Seq(1, 2, 3) -> 6)))
    println(read[Map[Seq[Int], Int]]("[[[1],1],[[1,2],3],[[1,2,3],6]]"))
    println(writeMsg(Map(Seq(1) -> 1, Seq(1, 2) -> 3, Seq(1, 2, 3) -> 6)))
    println(readBinary[Map[Seq[Int], Int]](upack.Obj(
      upack.Arr(upack.Int32(1)) -> upack.Int32(1),
      upack.Arr(upack.Int32(1), upack.Int32(2)) -> upack.Int32(3),
      upack.Arr(upack.Int32(1), upack.Int32(2), upack.Int32(3)) -> upack.Int32(6)
    )))
