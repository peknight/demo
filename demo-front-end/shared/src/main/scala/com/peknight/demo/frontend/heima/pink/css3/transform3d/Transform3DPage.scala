package com.peknight.demo.frontend.heima.pink.css3.transform3d

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class Transform3DPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def transform3D: Frag = simplePage("3D Transform")(
    style(Transform3DKeyFrameStyles.render[String]),
    link(rel := "stylesheet", href := "/css/transform3d.css")
  )(
    div(cls := "box")(
      div(cls := "trans-1")("trans-1"),
      div(cls := "trans-2")("trans-2"),
    ),
    div(cls := "box")(
      img(cls := "rotate-1-x", src := "/media/pig.jpg"),
      img(cls := "rotate-1-y", src := "/media/pig.jpg"),
      img(cls := "rotate-1-z", src := "/media/pig.jpg"),
      img(cls := "rotate-1-3d", src := "/media/pig.jpg")
    ),
    div(cls := "box")(
      div(cls := "style-1-outer")(
        div(cls := "style-1-inner"),
        div(cls := "style-1-inner"),
      ),
    ),
    div(cls := "box")(
      div(cls := "case-1-outer")(
        div(cls := "case-1-front")("正面"),
        div(cls := "case-1-back")("反面")
      )
    ),
    div(cls := "box")(
      ul(cls := "case-2")(for _ <- 1 to 2 yield li(div(cls := "case-2-outer")(
        div(cls := "case-2-front")("正面"),
        div(cls := "case-2-bottom")("下面")
      )))
    ),
    div(cls := "box")(
      ul(cls := "case-3")(for _ <- 1 to 2 yield li(div(cls := "case-3-outer")(
        div(cls := "case-3-front")("正面"),
        div(cls := "case-3-bottom")("下面")
      )))
    ),
    div(cls := "box")(
      div(cls := "case-4-outer")(
        section(cls := "case-4-inner")(for _ <- 1 to 6 yield div())
      )
    )
  )

end Transform3DPage
object Transform3DPage:
  object Text extends Transform3DPage(scalatags.Text)
end Transform3DPage
