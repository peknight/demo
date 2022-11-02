package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.bootstrap.{Carousel, CarouselConfig}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object BootstrapCarouselScript:

  @JSExportTopLevel("bootstrapCarousel")
  def bootstrapCarousel(): Unit =
    val carouselElement = dom.document.querySelector("#focus-carousel").asInstanceOf[dom.HTMLElement]
    val carousel = new Carousel(carouselElement, new CarouselConfig {
      interval = 2000
      wrap = false
    })

