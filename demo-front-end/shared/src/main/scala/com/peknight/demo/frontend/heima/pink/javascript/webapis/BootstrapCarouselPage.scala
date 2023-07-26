package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.BootstrapCarouselPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class BootstrapCarouselPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def links: Seq[String] = Seq("/webjars/bootstrap/5.3.0/dist/css/bootstrap.min.css")
  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def scripts: Seq[String] = Seq("/webjars/bootstrap/5.3.0/dist/js/bootstrap.bundle.min.js")
  override def headTitle: String = "bootstrap轮播图"
  private[this] val focusId: String = "focus-carousel"
  override def bodyFrag = div(cls := "focus")(
    div(id := focusId, cls := "carousel slide", attr("data-bs-ride") := "carousel", attr("data-bs-interval") := 2000)(
      div(cls := "carousel-indicators")(
        button(
          `type` := "button", attr("data-bs-target") := s"#$focusId", attr("data-bs-slide-to") := "0", cls := "active",
          attr("aria-current") :="true", attr("aria-label") := "Slide 1"
        ),
        for i <- 1 to 3 yield
          button(
            `type` := "button", attr("data-bs-target") := s"#$focusId", attr("data-bs-slide-to") := s"$i",
            attr("aria-label") := s"Slide ${i + 1}"
          ),
      ),
      div(cls :="carousel-inner")(
        div(cls := "carousel-item active")(img(src := "/jd/upload/banner.dpg", cls := "d-block w-100")),
        for i <- 1 to 3 yield div(cls := "carousel-item")(img(src := s"/jd/upload/banner$i.dpg", cls := "d-block w-100")),
      ),
      button(cls := "carousel-control-prev", `type` := "button", attr("data-bs-target") := s"#$focusId", attr("data-bs-slide") := "prev")(
        span(cls := "carousel-control-prev-icon", attr("aria-hidden") := "true"),
        span(cls := "visually-hidden")("Previous"),
      ),
      button(cls := "carousel-control-next", `type` := "button", attr("data-bs-target") := s"#$focusId", attr("data-bs-slide") := "next")(
        span(cls := "carousel-control-next-icon", attr("aria-hidden") := "true"),
        span(cls := "visually-hidden")("Next"),
      ),
    )
  )

end BootstrapCarouselPage
object BootstrapCarouselPage:
  object Text extends BootstrapCarouselPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".focus" - (
      width(800.px),
      height(300.px),
      backgroundColor.pink,
      margin(100.px, auto)
    )
    "img" - (
      width(800.px),
      height(300.px)
    )
  end Styles
end BootstrapCarouselPage
