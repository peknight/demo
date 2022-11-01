package com.peknight.demo.frontend.swiper

import scala.scalajs.js

trait SwiperOptions extends js.Object:
  // 264
  var spaceBetween: js.UndefOr[Double] = js.undefined
  // 273
  var slidesPerView: js.UndefOr[Double | "auto"] = js.undefined
  // 321
  var centeredSlides: js.UndefOr[Boolean] = js.undefined
  // 610
  var loop: js.UndefOr[Boolean] = js.undefined
  // 899
  var autoplay: js.UndefOr[AutoplayOptions | Boolean] = js.undefined
  // 1106
  var navigation: js.UndefOr[NavigationOptions | Boolean] = js.undefined
  // 1121
  var pagination: js.UndefOr[PaginationOptions | Boolean] = js.undefined

