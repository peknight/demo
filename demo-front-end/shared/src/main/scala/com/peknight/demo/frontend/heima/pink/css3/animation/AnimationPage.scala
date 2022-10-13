package com.peknight.demo.frontend.heima.pink.css3.animation

import com.peknight.demo.frontend.page.BasePage
import scalatags.generic.Bundle

class AnimationPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def animation: Frag = simplePage("Animation")(AnimationStyles)(
    // 位移
    div(cls := "box")(
    ),
  )

end AnimationPage
object AnimationPage:
  object Text extends AnimationPage(scalatags.Text)
end AnimationPage
