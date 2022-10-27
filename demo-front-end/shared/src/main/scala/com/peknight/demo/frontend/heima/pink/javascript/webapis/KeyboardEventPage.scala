package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class KeyboardEventPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def headTitle: String = "键盘事件"
  override def javaScriptMethod: Option[String] = Some("keyboardEvent")

end KeyboardEventPage
object KeyboardEventPage:
  object Text extends KeyboardEventPage(scalatags.Text)
end KeyboardEventPage
