package com.peknight.demo.frontend.apache.echarts.component.toolbox.feature

import com.peknight.demo.frontend.apache.echarts.component.toolbox.{IconStyle, IconStyleMixin, ToolboxFeatureOption}
import com.peknight.demo.frontend.apache.echarts.util.{DisplayState, ZRColor}
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait ToolboxSaveAsImageFeatureOption extends ToolboxFeatureOption:
  val `type`: js.UndefOr["png" | "jpeg"] = js.undefined
  val backgroundColor: js.UndefOr[ZRColor] = js.undefined
  val connectedBackgroundColor: js.UndefOr[ZRColor] = js.undefined
  val name: js.UndefOr[String] = js.undefined
  val excludeComponents: js.UndefOr[js.Array[String]] = js.undefined
  val pixelRatio: js.UndefOr[Number] = js.undefined
  val lang: js.UndefOr[js.Array[String]] = js.undefined

object ToolboxSaveAsImageFeatureOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            title: js.UndefOr[String] = js.undefined,
            icon: js.UndefOr[String] = js.undefined,
            iconStyle: js.UndefOr[IconStyle] = js.undefined,
            emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = js.undefined,
            iconStatus: js.UndefOr[DisplayState] = js.undefined,
            onclick: js.UndefOr[js.Function0[Unit]] = js.undefined,
            `type`: js.UndefOr["png" | "jpeg"] = js.undefined,
            backgroundColor: js.UndefOr[ZRColor] = js.undefined,
            connectedBackgroundColor: js.UndefOr[ZRColor] = js.undefined,
            name: js.UndefOr[String] = js.undefined,
            excludeComponents: js.UndefOr[js.Array[String]] = js.undefined,
            pixelRatio: js.UndefOr[Number] = js.undefined,
            lang: js.UndefOr[js.Array[String]] = js.undefined): ToolboxSaveAsImageFeatureOption =
    val _show: js.UndefOr[Boolean] = show
    val _title: js.UndefOr[String] = title
    val _icon: js.UndefOr[String] = icon
    val _iconStyle: js.UndefOr[IconStyle] = iconStyle
    val _emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = emphasis
    val _iconStatus: js.UndefOr[DisplayState] = iconStatus
    val _onclick: js.UndefOr[js.Function0[Unit]] = onclick
    val _type: js.UndefOr["png" | "jpeg"] = `type`
    val _backgroundColor: js.UndefOr[ZRColor] = backgroundColor
    val _connectedBackgroundColor: js.UndefOr[ZRColor] = connectedBackgroundColor
    val _name: js.UndefOr[String] = name
    val _excludeComponents: js.UndefOr[js.Array[String]] = excludeComponents
    val _pixelRatio: js.UndefOr[Number] = pixelRatio
    val _lang: js.UndefOr[js.Array[String]] = lang
    val toolboxSaveAsImageFeatureOption: ToolboxSaveAsImageFeatureOption = new ToolboxSaveAsImageFeatureOption:
      override val show: js.UndefOr[Boolean] = _show
      override val title: js.UndefOr[String] = _title
      override val icon: js.UndefOr[String] = _icon
      override val iconStyle: js.UndefOr[IconStyle] = _iconStyle
      override val emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = _emphasis
      override val iconStatus: js.UndefOr[DisplayState] = _iconStatus
      override val onclick: js.UndefOr[js.Function0[Unit]] = _onclick
      override val `type`: js.UndefOr["png" | "jpeg"] = _type
      override val backgroundColor: js.UndefOr[ZRColor] = _backgroundColor
      override val connectedBackgroundColor: js.UndefOr[ZRColor] = _connectedBackgroundColor
      override val name: js.UndefOr[String] = _name
      override val excludeComponents: js.UndefOr[js.Array[String]] = _excludeComponents
      override val pixelRatio: js.UndefOr[Number] = _pixelRatio
      override val lang: js.UndefOr[js.Array[String]] = _lang
    toolboxSaveAsImageFeatureOption.clean  
