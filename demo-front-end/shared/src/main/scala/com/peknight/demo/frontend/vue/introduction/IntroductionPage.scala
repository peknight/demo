package com.peknight.demo.frontend.vue.introduction

import com.peknight.demo.frontend.vue.page.VuePage
import scalatags.generic.Bundle

class IntroductionPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends VuePage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override protected def headTitle: String = "快速上手 - 简介"

  override protected def bodyFrag = div(id := "app")(button(attr("@click", raw = true) := "count++")("Count is: {{ count }}"))

  override protected def javaScriptMethod: Option[String] = Some("introduction")

end IntroductionPage
object IntroductionPage:
  object Text extends IntroductionPage(scalatags.Text)
end IntroductionPage
