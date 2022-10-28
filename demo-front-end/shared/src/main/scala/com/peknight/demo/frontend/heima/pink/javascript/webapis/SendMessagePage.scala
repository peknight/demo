package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SendMessagePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def headTitle: String = "发送短信案例"
  override def javaScriptMethod: Option[String] = Some("sendMessage")
  override def bodyFrag = frag(
    "手机号码：",
    input(`type` := "number"),
    button("发送")
  )

end SendMessagePage
object SendMessagePage:
  object Text extends SendMessagePage(scalatags.Text)
end SendMessagePage
