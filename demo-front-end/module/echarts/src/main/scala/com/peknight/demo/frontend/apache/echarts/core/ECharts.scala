package com.peknight.demo.frontend.apache.echarts.core

import com.peknight.demo.frontend.apache.echarts.core.SetOptionOpts
import com.peknight.demo.frontend.apache.echarts.util.ECBasicOption
import org.scalajs.dom.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class ECharts extends js.Object:
  def setOption[Opt <: ECBasicOption](option: Opt, opts: js.UndefOr[SetOptionOpts] = js.undefined): Unit = js.native
  def resize(opts: js.UndefOr[ResizeOpts] = js.undefined): Unit = js.native

@js.native
@JSGlobal("echarts")
object ECharts extends js.Object:
  def init(dom: HTMLElement, theme: js.UndefOr[String | js.Object] = js.undefined,
           opts: js.UndefOr[EChartsInitOpts] = js.undefined): EChartsType = js.native
