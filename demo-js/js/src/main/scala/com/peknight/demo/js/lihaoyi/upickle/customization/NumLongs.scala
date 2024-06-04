package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.core.Visitor

object NumLongs extends upickle.AttributeTagged:
  override implicit val LongWriter = new Writer[Long]:
    def write0[V](out: Visitor[?, V], v: Long) = out.visitFloat64String(v.toString, -1)

