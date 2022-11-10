package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait EChartsInitOpts extends js.Object:
  val locale: js.UndefOr[String | LocaleOption] = js.undefined
  val renderer: js.UndefOr[RendererType] = js.undefined
  val devicePixelRatio: js.UndefOr[Number] = js.undefined
  val useDirtyRect: js.UndefOr[Boolean] = js.undefined
  val useCoarsePointer: js.UndefOr[Boolean] = js.undefined
  val pointerSize: js.UndefOr[Number] = js.undefined
  val ssr: js.UndefOr[Boolean] = js.undefined
  val width: js.UndefOr[Number] = js.undefined
  val height: js.UndefOr[Number] = js.undefined
