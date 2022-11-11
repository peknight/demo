package com.peknight.demo.frontend.apache.echarts.component.timeline

import com.peknight.demo.frontend.apache.echarts.util.{BoxLayoutOptionMixin, ComponentOption, OptionName, SymbolOptionMixin}

import scala.scalajs.js

// TODO
trait TimelineOption extends js.Object with ComponentOption with BoxLayoutOptionMixin with SymbolOptionMixin:
  type Type = String
  type NameType = OptionName
