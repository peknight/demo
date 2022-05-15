package com.peknight.demo.js.lihaoyi.upickle.upack

import com.peknight.demo.js.lihaoyi.upickle.basics.{Big, Thing}
import ujson.StringRenderer

object UpackApp:

  def upackDemo() =
    val msg0 = upack.Arr(
      upack.Obj(upack.Str("myFieldA") -> upack.Int32(1), upack.Str("myFieldB") -> upack.Str("g")),
      upack.Obj(upack.Str("myFieldA") -> upack.Int32(2), upack.Str("myFieldB") -> upack.Str("k"))
    )
    println(msg0)
    val binary0: Array[Byte] = upack.write(msg0)
    val read = upack.read(binary0)
    given CanEqual[upack.Arr, upack.Msg] = CanEqual.derived
    println(msg0 == read)

    val big = Big(1, true, "lol", 'Z', Thing(7, ""))
    val msg1: upack.Msg = upickle.default.writeMsg(big)
    println(upickle.default.readBinary[Big](msg1))

    val msgSeq = Seq[upack.Msg](upack.Str("hello world"), upack.Arr(upack.Int32(1), upack.Int32(2)))

    val binary1: Array[Byte] = upickle.default.writeBinary(msgSeq)

    upickle.default.readBinary[Seq[upack.Msg]](binary1)

    println(upack.transform(msg0, StringRenderer()).toString)
    println(upack.transform(binary0, StringRenderer()).toString)

    val msg2 = upack.Obj(upack.Arr(upack.Int32(1), upack.Int32(2)) -> upack.Int32(1))
    println(upack.transform(msg2, StringRenderer()).toString)
