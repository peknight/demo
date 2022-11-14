package com.peknight.demo.frontend.apache.echarts.coord.cartesian

import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait GridOption extends js.Object with ComponentOption with BoxLayoutOptionMixin with ShadowOptionMixin:
  type Type = String
  type MainType = "grid"
  type NameType = OptionName

  val show: js.UndefOr[Boolean] = js.undefined
  val containLabel: js.UndefOr[Boolean] = js.undefined
  val backgroundColor: js.UndefOr[ZRColor] = js.undefined
  val borderWidth: js.UndefOr[Number] = js.undefined
  val borderColor: js.UndefOr[ZRColor] = js.undefined
  val tooltip: js.UndefOr[js.Any] = js.undefined

object GridOption:
  def apply(
            `type`: js.UndefOr[String] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            width: js.UndefOr[Number | String] = js.undefined,
            height: js.UndefOr[Number | String] = js.undefined,
            top: js.UndefOr[Number | String] = js.undefined,
            right: js.UndefOr[Number | String] = js.undefined,
            bottom: js.UndefOr[Number | String] = js.undefined,
            left: js.UndefOr[Number | String] = js.undefined,
            shadowBlur: js.UndefOr[Number] = js.undefined,
            shadowColor: js.UndefOr[ColorString] = js.undefined,
            shadowOffsetX: js.UndefOr[Number] = js.undefined,
            shadowOffsetY: js.UndefOr[Number] = js.undefined,
            show: js.UndefOr[Boolean] = js.undefined,
            containLabel: js.UndefOr[Boolean] = js.undefined,
            backgroundColor: js.UndefOr[ZRColor] = js.undefined,
            borderWidth: js.UndefOr[Number] = js.undefined,
            borderColor: js.UndefOr[ZRColor] = js.undefined,
            tooltip: js.UndefOr[js.Any] = js.undefined,
           ): GridOption =
    val _type: js.UndefOr[String] = `type`
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _width: js.UndefOr[Number | String] = width
    val _height: js.UndefOr[Number | String] = height
    val _top: js.UndefOr[Number | String] = top
    val _right: js.UndefOr[Number | String] = right
    val _bottom: js.UndefOr[Number | String] = bottom
    val _left: js.UndefOr[Number | String] = left
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _show: js.UndefOr[Boolean] = show
    val _containLabel: js.UndefOr[Boolean] = containLabel
    val _backgroundColor: js.UndefOr[ZRColor] = backgroundColor
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _borderColor: js.UndefOr[ZRColor] = borderColor
    val _tooltip: js.UndefOr[js.Any] = tooltip
    val gridOption = new GridOption:
      override val mainType: js.UndefOr[MainType] = "grid"
      override val `type`: js.UndefOr[Type] = _type
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val width: js.UndefOr[Number | String] = _width
      override val height: js.UndefOr[Number | String] = _height
      override val top: js.UndefOr[Number | String] = _top
      override val right: js.UndefOr[Number | String] = _right
      override val bottom: js.UndefOr[Number | String] = _bottom
      override val left: js.UndefOr[Number | String] = _left
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val show: js.UndefOr[Boolean] = _show
      override val containLabel: js.UndefOr[Boolean] = _containLabel
      override val backgroundColor: js.UndefOr[ZRColor] = _backgroundColor
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val borderColor: js.UndefOr[ZRColor] = _borderColor
      override val tooltip: js.UndefOr[js.Any] = _tooltip
    gridOption.clean
