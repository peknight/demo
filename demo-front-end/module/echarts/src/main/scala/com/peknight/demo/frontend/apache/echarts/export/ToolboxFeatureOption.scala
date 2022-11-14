package com.peknight.demo.frontend.apache.echarts.`export`

import com.peknight.demo.frontend.apache.echarts.clean
import com.peknight.demo.frontend.apache.echarts.component.toolbox.feature.*
import com.peknight.demo.frontend.apache.echarts.component.toolbox.{IconStyle, IconStyleMixin}
import com.peknight.demo.frontend.apache.echarts.util.DisplayState

import scala.scalajs.js

trait ToolboxFeatureOption extends com.peknight.demo.frontend.apache.echarts.component.toolbox.ToolboxFeatureOption:
  val brush: js.UndefOr[ToolboxBrushFeatureOption] = js.undefined
  val dataView: js.UndefOr[ToolboxDataViewFeatureOption] = js.undefined
  val dataZoom: js.UndefOr[ToolboxDataZoomFeatureOption] = js.undefined
  val magicType: js.UndefOr[ToolboxMagicTypeFeatureOption] = js.undefined
  val restore: js.UndefOr[ToolboxRestoreFeatureOption] = js.undefined
  val saveAsImage: js.UndefOr[ToolboxSaveAsImageFeatureOption] = js.undefined

object ToolboxFeatureOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            title: js.UndefOr[String] = js.undefined,
            icon: js.UndefOr[String] = js.undefined,
            iconStyle: js.UndefOr[IconStyle] = js.undefined,
            emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = js.undefined,
            iconStatus: js.UndefOr[DisplayState] = js.undefined,
            onclick: js.UndefOr[js.Function0[Unit]] = js.undefined,
            brush: js.UndefOr[ToolboxBrushFeatureOption] = js.undefined,
            dataView: js.UndefOr[ToolboxDataViewFeatureOption] = js.undefined,
            dataZoom: js.UndefOr[ToolboxDataZoomFeatureOption] = js.undefined,
            magicType: js.UndefOr[ToolboxMagicTypeFeatureOption] = js.undefined,
            restore: js.UndefOr[ToolboxRestoreFeatureOption] = js.undefined,
            saveAsImage: js.UndefOr[ToolboxSaveAsImageFeatureOption] = js.undefined): ToolboxFeatureOption =
    val _show: js.UndefOr[Boolean] = show
    val _title: js.UndefOr[String] = title
    val _icon: js.UndefOr[String] = icon
    val _iconStyle: js.UndefOr[IconStyle] = iconStyle
    val _emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = emphasis
    val _iconStatus: js.UndefOr[DisplayState] = iconStatus
    val _onclick: js.UndefOr[js.Function0[Unit]] = onclick
    val _brush: js.UndefOr[ToolboxBrushFeatureOption] = brush
    val _dataView: js.UndefOr[ToolboxDataViewFeatureOption] = dataView
    val _dataZoom: js.UndefOr[ToolboxDataZoomFeatureOption] = dataZoom
    val _magicType: js.UndefOr[ToolboxMagicTypeFeatureOption] = magicType
    val _restore: js.UndefOr[ToolboxRestoreFeatureOption] = restore
    val _saveAsImage: js.UndefOr[ToolboxSaveAsImageFeatureOption] = saveAsImage
    val toolboxFeatureOption = new ToolboxFeatureOption:
      override val show: js.UndefOr[Boolean] = _show
      override val title: js.UndefOr[String] = _title
      override val icon: js.UndefOr[String] = _icon
      override val iconStyle: js.UndefOr[IconStyle] = _iconStyle
      override val emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = _emphasis
      override val iconStatus: js.UndefOr[DisplayState] = _iconStatus
      override val onclick: js.UndefOr[js.Function0[Unit]] = _onclick
      override val brush: js.UndefOr[ToolboxBrushFeatureOption] = _brush
      override val dataView: js.UndefOr[ToolboxDataViewFeatureOption] = _dataView
      override val dataZoom: js.UndefOr[ToolboxDataZoomFeatureOption] = _dataZoom
      override val magicType: js.UndefOr[ToolboxMagicTypeFeatureOption] = _magicType
      override val restore: js.UndefOr[ToolboxRestoreFeatureOption] = _restore
      override val saveAsImage: js.UndefOr[ToolboxSaveAsImageFeatureOption] = _saveAsImage
    toolboxFeatureOption.clean
