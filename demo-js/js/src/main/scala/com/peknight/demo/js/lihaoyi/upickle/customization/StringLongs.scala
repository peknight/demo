package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.core.Visitor

object StringLongs extends upickle.AttributeTagged:

  override implicit val LongWriter: Writer[Long] = new Writer[Long]:
    def write0[V](out: Visitor[?, V], v: Long) = out.visitString(v.toString, -1)
