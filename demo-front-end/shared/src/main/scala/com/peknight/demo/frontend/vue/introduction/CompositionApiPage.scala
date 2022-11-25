package com.peknight.demo.frontend.vue.introduction

import com.peknight.demo.frontend.vue.page.VuePage
import scalatags.generic.Bundle

class CompositionApiPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends VuePage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override protected def headTitle: String = "快速上手 - 组合式API"

  override protected def javaScriptMethod: Option[String] = Some("compositionApi")

  override protected def bodyFrag = div(id := "app")(button(attr("@click", raw = true) := "increment")("Count is: {{ count }}"))

end CompositionApiPage
object CompositionApiPage:
  object Text extends CompositionApiPage(scalatags.Text)
end CompositionApiPage
