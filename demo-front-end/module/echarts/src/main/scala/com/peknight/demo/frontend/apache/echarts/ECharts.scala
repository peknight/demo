package com.peknight.demo.frontend.apache.echarts

import org.scalajs.dom.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class ECharts extends js.Object:
  def setOption[Opt <: ECBasicOption](option: Opt, opts: js.UndefOr[SetOptionOpts] = js.undefined): Unit = js.native

@js.native
@JSGlobal("echarts")
object ECharts extends js.Object:
  def init(dom: HTMLElement, theme: js.UndefOr[String | js.Object] = js.undefined,
           opts: js.UndefOr[EChartsInitOpts] = js.undefined): EChartsType = js.native
