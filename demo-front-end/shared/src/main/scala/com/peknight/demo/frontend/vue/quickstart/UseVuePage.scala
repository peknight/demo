package com.peknight.demo.frontend.vue.quickstart

import com.peknight.demo.frontend.vue.page.VuePage
import scalatags.generic.Bundle

class UseVuePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends VuePage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override protected def headTitle: String = "快速上手 - 使用Vue"

  override protected def bodyFrag = div(id := "app")("{{ message }}")

  override protected def javaScriptMethod: Option[String] = Some("useVue")

end UseVuePage
object UseVuePage:
  object Text extends UseVuePage(scalatags.Text)
end UseVuePage
