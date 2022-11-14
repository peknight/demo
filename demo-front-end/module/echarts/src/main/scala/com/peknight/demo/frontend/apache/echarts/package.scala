package com.peknight.demo.frontend.apache

import scala.scalajs.js

package object echarts:
  type Number = Double | Int

  type CanvasLineCap = "butt" | "round" | "square"
  type CanvasLineJoin = "bevel" | "miter" | "round"

  extension [T <: js.Any] (t: T)
    def clean: T = t match
      case array if js.Array.isArray(array) =>
        array.asInstanceOf[js.Array[js.Any]].filterNot(js.isUndefined).map(_.clean).asInstanceOf[T]
      case obj: js.Object =>
        val newObj: js.Object = new js.Object()
        js.Object.keys(obj)
          .filterNot(key => js.isUndefined(obj.asInstanceOf[js.Dynamic].selectDynamic(key)))
          .map(key => key -> obj.asInstanceOf[js.Dynamic].selectDynamic(key).clean)
          .foreach { case (key, value) => newObj.asInstanceOf[js.Dynamic].updateDynamic(key)(value) }
        newObj.asInstanceOf[T]
      case other => other
  end extension
