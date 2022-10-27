package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalatags.generic.Bundle

class JingdongKeyUpFocusPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def headTitle: String = "模拟京东按键输入内容"
  override def javaScriptMethod: Option[String] = Some("jdFocus")
  override def bodyFrag = input(`type` := "text")

end JingdongKeyUpFocusPage
object JingdongKeyUpFocusPage:
  object Text extends JingdongKeyUpFocusPage(scalatags.Text)
end JingdongKeyUpFocusPage
