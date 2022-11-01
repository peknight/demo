package com.peknight.demo.frontend.swiper

import scala.scalajs.js
import org.scalajs.dom.HTMLElement

trait PaginationOptions extends js.Object:

  var enabled: js.UndefOr[Boolean] = js.undefined
  var el: js.UndefOr[CSSSelector | HTMLElement] = js.undefined
  var `type`: js.UndefOr["bullets" | "fraction" | "progressbar" | "custom"] = js.undefined
  var bulletElement: js.UndefOr[String] = js.undefined
  var dynamicBullets: js.UndefOr[Boolean] = js.undefined
  var dynamicMainBullets: js.UndefOr[Double] = js.undefined
  var hideOnClick: js.UndefOr[Boolean] = js.undefined
  var clickable: js.UndefOr[Boolean] = js.undefined
  var progressbarOpposite: js.UndefOr[Boolean] = js.undefined
  var formatFractionCurrent: js.UndefOr[js.Function1[Double, Double | String]] = js.undefined
  var formatFractionTotal: js.UndefOr[js.Function1[Double, Double | String]] = js.undefined
  var renderBullet: js.UndefOr[js.Function2[Double, String, Unit]] = js.undefined
  var renderFraction: js.UndefOr[js.Function2[String, String, Unit]] = js.undefined
  var renderProgressbar: js.UndefOr[js.Function1[String, Unit]] = js.undefined
  var renderCustom: js.UndefOr[js.Function3[Swiper, Double, Double, Unit]] = js.undefined
  var bulletClass: js.UndefOr[String] = js.undefined
  var bulletActiveClass: js.UndefOr[String] = js.undefined
  var modifierClass: js.UndefOr[String] = js.undefined
  var currentClass: js.UndefOr[String] = js.undefined
  var totalClass: js.UndefOr[String] = js.undefined
  var hiddenClass: js.UndefOr[String] = js.undefined
  var progressbarFillClass: js.UndefOr[String] = js.undefined
  var progressbarOppositeClass: js.UndefOr[String] = js.undefined
  var clickableClass: js.UndefOr[String] = js.undefined
  var lockClass: js.UndefOr[String] = js.undefined
  var horizontalClass: js.UndefOr[String] = js.undefined
  var verticalClass: js.UndefOr[String] = js.undefined
  var paginationDisabledClass: js.UndefOr[String] = js.undefined
