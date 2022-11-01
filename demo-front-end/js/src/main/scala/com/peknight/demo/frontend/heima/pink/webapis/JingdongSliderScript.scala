package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.swiper.{AutoplayOptions, NavigationOptions, PaginationOptions, Swiper, SwiperOptions}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JingdongSliderScript:

  @JSExportTopLevel("jingdongSlider")
  def jingdongSlider(): Unit =
    dom.document.addEventListener("DOMContentLoaded", _ => {
      new Swiper(".swiper", new SwiperOptions {
        spaceBetween = 30
        centeredSlides = true
        autoplay = new AutoplayOptions {
          delay = 2500
          disableOnInteraction = false
        }
        pagination = new PaginationOptions {
          el = ".swiper-pagination"
          clickable = true
        }
        loop = true
      })
    })
