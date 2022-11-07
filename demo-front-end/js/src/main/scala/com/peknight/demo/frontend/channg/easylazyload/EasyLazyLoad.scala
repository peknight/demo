package com.peknight.demo.frontend.channg.easylazyload

import org.querki.jquery.*
import org.scalajs.dom
import scalatags.JsDom.all.*

import scala.scalajs.js

object EasyLazyLoad:

  def lazyLoadInit(setting: LazyLoadOptions): Unit =
    val lazyImgList: JQuery = $("img[data-lazy-src]")
    lazyImgList.each(e =>
      $(e).attr("src", "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==")
    )
    val windowHeight: Double = $(dom.window).height()
    checkOffset(setting, lazyImgList, windowHeight)
    dom.window.addEventListener("scroll", _ => checkOffset(setting, lazyImgList, windowHeight))

  def checkOffset(setting: LazyLoadOptions, lazyImgList: JQuery, windowHeight: Double): Unit =
    val scrollTop = $(dom.document).scrollTop()
    lazyImgList.get().to(LazyList).map(e => $(e.asInstanceOf[dom.Element])).zipWithIndex
      .filter{ case (e, _) => e.attr("data-comp").isEmpty &&
        e.offset().top - scrollTop + setting.offsetTopm >= 0 &&
        e.offset().top - scrollTop < (windowHeight + setting.offsetBottom)
      }
      .map(_._2).headOption
      .foreach(i => showImg(setting, lazyImgList, windowHeight, i))

  def showImg(setting: LazyLoadOptions, lazyImgList: JQuery, windowHeight: Double, index: Int): Unit =
    val jDom: JQuery = lazyImgList.eq(index)
    val srcValue = jDom.attr("data-lazy-src").get
    img(src := srcValue).render.onload = _ => {
      try
        if jDom.attr("data-comp").isEmpty then
          jDom.attr("src", srcValue)
          $("body").append(div(
            cls := s"mask-lazy-div$index",
            style := s"background-color:${setting.coverColor};position:absolute;width:${jDom.width()}px;" +
              s"height:${jDom.height()}px;top:${jDom.offset().top}px;left:${jDom.offset().left}px;z-index:500"
          )(setting.coverDiv).render)
          $(s".mask-lazy-div$index").animate(
            js.Dictionary[js.Any]("opacity" -> "0"),
            setting.showTime, "",
            e => {
              $(e).remove()
              setting.onLoadBackEnd(index, jDom)
              checkOffset(setting, lazyImgList, windowHeight)
            }
          )
          jDom.attr("data-comp", "true")
      catch case _ => ()
      setting.onLoadBackStart(index, jDom)
    }