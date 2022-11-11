package com.peknight.demo.frontend.apache.echarts.component

import com.peknight.demo.frontend.apache.echarts.util.CallbackDataParams

import scala.scalajs.js

package object tooltip:
  type TopLevelFormatterParams = CallbackDataParams | js.Array[CallbackDataParams]
