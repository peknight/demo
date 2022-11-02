package com.peknight.demo.frontend.heima.pink.javascript.jquery

import scalatags.generic.Bundle

class JQueryDomConvertPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def headTitle: String = "DOM对象和jQuery对象相互转换"
  override def javaScriptMethod: Option[String] = Some("jQueryDomConvert")
  override def bodyFrag = video(src := "/webapis/videos/mov.mp4", attr("muted") := true)

end JQueryDomConvertPage
object JQueryDomConvertPage:
  object Text extends JQueryDomConvertPage(scalatags.Text)
end JQueryDomConvertPage
