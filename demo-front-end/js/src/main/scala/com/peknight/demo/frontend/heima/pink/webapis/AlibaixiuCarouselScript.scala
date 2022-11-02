package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.bootstrap.{Carousel, CarouselConfig}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object AlibaixiuCarouselScript:

  @JSExportTopLevel("alibaixiuCarousel")
  def alibaixiuCarousel(): Unit =
    dom.document.addEventListener("DOMContentLoaded", _ => {
      val alibaixiuCarouselElement = dom.document.querySelector("#alibaixiu-carousel").asInstanceOf[dom.HTMLElement]
      val alibaixiuCarousel = new Carousel(alibaixiuCarouselElement, new CarouselConfig {
        interval = 3000
        wrap = false
      })
    })

