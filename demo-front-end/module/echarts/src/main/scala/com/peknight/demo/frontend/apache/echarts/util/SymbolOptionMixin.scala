package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait SymbolOptionMixin[T] extends js.Object:
  /**
   * type of symbol, like `cirlce`, `rect`, or custom path and image.
   */
  val symbol: js.UndefOr[String | SymbolCallback[T]] = js.undefined
  /**
   * Size of symbol.
   */
  val symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[T]] = js.undefined
  val symbolRotate: js.UndefOr[Number | SymbolRotateCallback[T]] = js.undefined
  val symbolKeepAspect: js.UndefOr[Boolean] = js.undefined
  val symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[T]] = js.undefined
