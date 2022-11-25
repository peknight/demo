package com.peknight.demo.frontend.vue.introduction

import com.peknight.demo.frontend.vue.page.VuePage
import scalatags.generic.Bundle

class OptionsApiPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends VuePage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override protected def headTitle: String = "快速上手 - 选项式API"

  override protected def javaScriptMethod: Option[String] = Some("optionsApi")

  override protected def bodyFrag = div(id := "app")(button(attr("@click", raw = true) := "increment")("Count is: {{ count }}"))

end OptionsApiPage
object OptionsApiPage:
  object Text extends OptionsApiPage(scalatags.Text)
end OptionsApiPage
