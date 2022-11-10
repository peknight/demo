package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js
import scala.scalajs.js.UndefOr

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
  def apply(text: js.UndefOr[String] = js.undefined): TitleOption =
    val _text = text
    new TitleOption {
      override val text: js.UndefOr[String] = _text
    }

