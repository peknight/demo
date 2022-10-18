package com.peknight.demo.frontend.heima.pink.mobile.media

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class MediaQueryPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val demo1: Frag = simplePage("媒体查询 背景颜色随屏幕宽度变化")(style(Demo1MediaStyles.render[String]))()

  val demo2: Frag = simplePage("媒体查询 购物车随屏幕宽度变化")(
    style(Demo2MediaStyles.render[String]),
    style(Demo2Styles.render[String])
  )(
    div(cls := "top")("购物车")
  )

  /**
   * 当我们屏幕大于等于640px以上的，div一行显示两个
   * 小于640 div一行显示一个
   * 一个建议，我们媒体查询最好的方法是从小到大
   */
  val demo3: Frag = simplePage("媒体查询 引入资源")(
    // 引入资源就是针对不同的屏幕尺寸，调用不同的css文件
    link(rel := "stylesheet", href := "/css/media3w320.css", media := "screen and (min-width: 320px)"),
    link(rel := "stylesheet", href := "/css/media3w640.css", media := "screen and (min-width: 640px)")
  )(
    div(1),
    div(2)
  )

end MediaQueryPage
object MediaQueryPage:
  object Text extends MediaQueryPage(scalatags.Text)
end MediaQueryPage

