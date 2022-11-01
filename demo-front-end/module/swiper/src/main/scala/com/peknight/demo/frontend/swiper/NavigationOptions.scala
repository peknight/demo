package com.peknight.demo.frontend.swiper

import scala.scalajs.js
import org.scalajs.dom.HTMLElement

trait NavigationOptions extends js.Object:
  var enabled: js.UndefOr[Boolean] = js.undefined;
  var nextEl: js.UndefOr[CSSSelector | HTMLElement] = js.undefined
  var prevEl: js.UndefOr[CSSSelector | HTMLElement] = js.undefined
  var hideOnClick: js.UndefOr[Boolean] = js.undefined
  var disabledClass: js.UndefOr[String] = js.undefined
  var hiddenClass: js.UndefOr[String] = js.undefined
  var lockClass: js.UndefOr[String] = js.undefined
  var navigationDisabledClass: js.UndefOr[String] = js.undefined
