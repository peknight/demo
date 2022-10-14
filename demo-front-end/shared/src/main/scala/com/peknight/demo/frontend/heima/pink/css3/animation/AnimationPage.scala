package com.peknight.demo.frontend.heima.pink.css3.animation

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class AnimationPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def animation: Frag = simplePage("Animation")(
    style(AnimationKeyFrameStyles.render[String]),
    link(rel := "stylesheet", href := "css/animation.css"),
  )(
    // 位移
    div(cls := "box")(
      div(cls := "ani-1")("ani-1"),
    ),
    div(cls := "box")(
      div(cls := "ani-2")("ani-2"),
    ),
    div(cls := "box")(
      div(cls := "ani-3")("ani-3"),
    ),
    div(cls := "box")(
      div(cls := "ani-4")("ani-4"),
    ),
    div(cls := "box")(
      div(cls := "map")(Seq("beijing", "taipei", "guangzhou").map(loc => div(cls := s"city $loc")(
        div(cls := "dotted"),
        for i <- 1 to 3 yield div(cls := s"pulse-$i")
      )))
    ),
  )

end AnimationPage
object AnimationPage:
  object Text extends AnimationPage(scalatags.Text)
end AnimationPage
