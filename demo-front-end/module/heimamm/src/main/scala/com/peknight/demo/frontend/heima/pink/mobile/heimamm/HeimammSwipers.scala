package com.peknight.demo.frontend.heima.pink.mobile.heimamm

import com.peknight.demo.frontend.swiper.{NavigationOptions, Swiper, SwiperOptions}

object HeimammSwipers:

  def init: Unit =
    new Swiper(".get-job-fo", new SwiperOptions {
      slidesPerView = 2
      spaceBetween = 30
      centeredSlides = true
      loop = true
      navigation = new NavigationOptions {
        nextEl = ".swiper-button-next"
        prevEl = ".swiper-button-prev"
      }
    })
    new Swiper(".study-fo", new SwiperOptions {
      slidesPerView = 2.2
      spaceBetween = 20
    })
  end init
