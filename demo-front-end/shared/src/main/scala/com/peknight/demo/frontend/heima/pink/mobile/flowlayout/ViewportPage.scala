package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import scalatags.generic.Bundle

/**
 * width 宽度设置的是viewport宽度，可以设置device-width特殊值
 * initial-scale 初始缩放比，大于0的数字
 * maximum-scale 最大缩放比，大于0的数字
 * miniumn-scale 最小缩放比，大于0的数字
 * user-scalable 用户是否可以缩放，yes或no（1或0）
 */
class ViewportPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def viewportPage: Frag =
    html(lang := "en")(
      head(
        meta(charset := "UTF-8"),
        // 为移动端而生
        // meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(name := "viewport",
          content := "width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("Viewport"),
      ),
      body("注释掉viewport的meta标签用浏览器的移动端调试环境看字大小")
    )

end ViewportPage
object ViewportPage:
  object Text extends ViewportPage(scalatags.Text)
end ViewportPage


