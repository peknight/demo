package com.peknight.demo.frontend.heima.pink.css3.animation

import com.peknight.demo.frontend.page.BasePage
import scalatags.generic.Bundle

class AnimationPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def animation: Frag = simplePage("Animation")(AnimationKeyFrameStyles, AnimationStyles)(
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
  )

end AnimationPage
object AnimationPage:
  object Text extends AnimationPage(scalatags.Text)
end AnimationPage
