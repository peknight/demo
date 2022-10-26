package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalatags.generic.Bundle

class DataSetPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override def headTitle: String = "H5自定义属性"
  override def javaScriptMethod: Option[String] = Some("dataSet")
  override def bodyFrag = div(attr("getTime") := "20", attr("data-index") := "2", attr("data-list-name") := "andy")

end DataSetPage
object DataSetPage:
  object Text extends DataSetPage(scalatags.Text)
end DataSetPage
