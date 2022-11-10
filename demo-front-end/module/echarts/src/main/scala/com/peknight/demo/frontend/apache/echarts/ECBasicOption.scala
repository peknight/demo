package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait ECBasicOption extends ECUnitOption:
  type BaseOptionType <: ECUnitOption
  type OptionsType <: js.Array[BaseOptionType]
  type MediaType = js.Array[MediaUnit]

