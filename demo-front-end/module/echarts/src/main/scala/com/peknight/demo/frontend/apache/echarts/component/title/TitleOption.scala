package com.peknight.demo.frontend.apache.echarts.component.title

import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

import scala.scalajs.js

trait TitleOption extends js.Object with ComponentOption with BoxLayoutOptionMixin with BorderOptionMixin:
  type Type = String
  type MainType = "title"
  type NameType = OptionName
  val show: js.UndefOr[Boolean] = js.undefined
  val text: js.UndefOr[String] = js.undefined
  val link: js.UndefOr[String] = js.undefined
  val target: js.UndefOr["self" | "blank"] = js.undefined
  val subtext: js.UndefOr[String] = js.undefined
  val sublink: js.UndefOr[String] = js.undefined
  val subtarget: js.UndefOr["self" | "blank"] = js.undefined
  val textAlign: js.UndefOr[ZRTextAlign] = js.undefined
  val textVerticalAlign: js.UndefOr[ZRTextVerticalAlign] = js.undefined
  val textBaseline: js.UndefOr[ZRTextVerticalAlign] = js.undefined
  val backgroundColor: js.UndefOr[ZRColor] = js.undefined
  val padding: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val itemGap: js.UndefOr[Number] = js.undefined
  val textStyle: js.UndefOr[LabelOption] = js.undefined
  val subtextStyle: js.UndefOr[LabelOption] = js.undefined
  val triggerEvent: js.UndefOr[Boolean] = js.undefined
  val borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined

object TitleOption:
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
            borderColor: js.UndefOr[ZRColor] = js.undefined,
            borderWidth: js.UndefOr[Number] = js.undefined,
            borderType: js.UndefOr[ZRLineType] = js.undefined,
            borderCap: js.UndefOr[CanvasLineCap] = js.undefined,
            borderJoin: js.UndefOr[CanvasLineJoin] = js.undefined,
            borderDashOffset: js.UndefOr[Number] = js.undefined,
            borderMiterLimit: js.UndefOr[Number] = js.undefined,
            show: js.UndefOr[Boolean] = js.undefined,
            text: js.UndefOr[String] = js.undefined,
            link: js.UndefOr[String] = js.undefined,
            target: js.UndefOr["self" | "blank"] = js.undefined,
            subtext: js.UndefOr[String] = js.undefined,
            sublink: js.UndefOr[String] = js.undefined,
            subtarget: js.UndefOr["self" | "blank"] = js.undefined,
            textAlign: js.UndefOr[ZRTextAlign] = js.undefined,
            textVerticalAlign: js.UndefOr[ZRTextVerticalAlign] = js.undefined,
            textBaseline: js.UndefOr[ZRTextVerticalAlign] = js.undefined,
            backgroundColor: js.UndefOr[ZRColor] = js.undefined,
            padding: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            itemGap: js.UndefOr[Number] = js.undefined,
            textStyle: js.UndefOr[LabelOption] = js.undefined,
            subtextStyle: js.UndefOr[LabelOption] = js.undefined,
            triggerEvent: js.UndefOr[Boolean] = js.undefined,
            borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined): TitleOption =
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
    val _borderColor: js.UndefOr[ZRColor] = borderColor
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _borderType: js.UndefOr[ZRLineType] = borderType
    val _borderCap: js.UndefOr[CanvasLineCap] = borderCap
    val _borderJoin: js.UndefOr[CanvasLineJoin] = borderJoin
    val _borderDashOffset: js.UndefOr[Number] = borderDashOffset
    val _borderMiterLimit: js.UndefOr[Number] = borderMiterLimit
    val _show: js.UndefOr[Boolean] = show
    val _text: js.UndefOr[String] = text
    val _link: js.UndefOr[String] = link
    val _target: js.UndefOr["self" | "blank"] = target
    val _subtext: js.UndefOr[String] = subtext
    val _sublink: js.UndefOr[String] = sublink
    val _subtarget: js.UndefOr["self" | "blank"] = subtarget
    val _textAlign: js.UndefOr[ZRTextAlign] = textAlign
    val _textVerticalAlign: js.UndefOr[ZRTextVerticalAlign] = textVerticalAlign
    val _textBaseline: js.UndefOr[ZRTextVerticalAlign] = textBaseline
    val _backgroundColor: js.UndefOr[ZRColor] = backgroundColor
    val _padding: js.UndefOr[Number | js.Array[Number]] = padding
    val _itemGap: js.UndefOr[Number] = itemGap
    val _textStyle: js.UndefOr[LabelOption] = textStyle
    val _subtextStyle: js.UndefOr[LabelOption] = subtextStyle
    val _triggerEvent: js.UndefOr[Boolean] = triggerEvent
    val _borderRadius: js.UndefOr[Number | js.Array[Number]] = borderRadius
    new TitleOption:
      override val mainType: js.UndefOr[MainType] = "title"
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
      override val borderColor: js.UndefOr[ZRColor] = _borderColor
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val borderType: js.UndefOr[ZRLineType] = _borderType
      override val borderCap: js.UndefOr[CanvasLineCap] = _borderCap
      override val borderJoin: js.UndefOr[CanvasLineJoin] = _borderJoin
      override val borderDashOffset: js.UndefOr[Number] = _borderDashOffset
      override val borderMiterLimit: js.UndefOr[Number] = _borderMiterLimit
      override val show: js.UndefOr[Boolean] = _show
      override val text: js.UndefOr[String] = _text
      override val link: js.UndefOr[String] = _link
      override val target: js.UndefOr["self" | "blank"] = _target
      override val subtext: js.UndefOr[String] = _subtext
      override val sublink: js.UndefOr[String] = _sublink
      override val subtarget: js.UndefOr["self" | "blank"] = _subtarget
      override val textAlign: js.UndefOr[ZRTextAlign] = _textAlign
      override val textVerticalAlign: js.UndefOr[ZRTextVerticalAlign] = _textVerticalAlign
      override val textBaseline: js.UndefOr[ZRTextVerticalAlign] = _textBaseline
      override val backgroundColor: js.UndefOr[ZRColor] = _backgroundColor
      override val padding: js.UndefOr[Number | js.Array[Number]] = _padding
      override val itemGap: js.UndefOr[Number] = _itemGap
      override val textStyle: js.UndefOr[LabelOption] = _textStyle
      override val subtextStyle: js.UndefOr[LabelOption] = _subtextStyle
      override val triggerEvent: js.UndefOr[Boolean] = _triggerEvent
      override val borderRadius: js.UndefOr[Number | js.Array[Number]] = _borderRadius
