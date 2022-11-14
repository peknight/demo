package com.peknight.demo.frontend.apache.echarts.component.toolbox.feature

import com.peknight.demo.frontend.apache.echarts.component.toolbox.ToolboxFeatureOption
import com.peknight.demo.frontend.apache.echarts.util.{ColorString, ECUnitOption}
import org.scalajs.dom

import scala.scalajs.js

trait ToolboxDataViewFeatureOption extends ToolboxFeatureOption:
  val readOnly: js.UndefOr[Boolean] = js.undefined
  val optionToContent: js.UndefOr[js.Function1[ECUnitOption, String | dom.HTMLElement]] = js.undefined
  val contentToOption: js.UndefOr[js.Function2[dom.HTMLDivElement, ECUnitOption, ECUnitOption]] = js.undefined
  val lang: js.UndefOr[js.Array[String]] = js.undefined
  val backgroundColor: js.UndefOr[ColorString] = js.undefined
  val textColor: js.UndefOr[ColorString] = js.undefined
  val textareaColor: js.UndefOr[ColorString] = js.undefined
  val textareaBorderColor: js.UndefOr[ColorString] = js.undefined
  val buttonColor: js.UndefOr[ColorString] = js.undefined
  val buttonTextColor: js.UndefOr[ColorString] = js.undefined
