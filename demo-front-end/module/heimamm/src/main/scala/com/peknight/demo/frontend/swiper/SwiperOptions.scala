package com.peknight.demo.frontend.swiper

import scala.scalajs.js

trait SwiperOptions extends js.Object:
  var slidesPerView: js.UndefOr[Double | "auto"] = js.undefined
  var spaceBetween: js.UndefOr[Double] = js.undefined
  var centeredSlides: js.UndefOr[Boolean] = js.undefined
  var loop: js.UndefOr[Boolean] = js.undefined
  var navigation: js.UndefOr[NavigationOptions | Boolean] = js.undefined

