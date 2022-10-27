package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.AngelFollowMousePage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class AngelFollowMousePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "跟随鼠标的天使"
  override def javaScriptMethod: Option[String] = Some("angelFollowMouse")
  override def bodyFrag = img(src := "/webapis/images/angel.gif")

end AngelFollowMousePage
object AngelFollowMousePage:
  object Text extends AngelFollowMousePage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "img" - (position.absolute, top(2.px))
  end Styles
end AngelFollowMousePage
