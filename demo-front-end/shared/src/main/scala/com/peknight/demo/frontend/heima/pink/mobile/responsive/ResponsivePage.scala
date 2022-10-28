package com.peknight.demo.frontend.heima.pink.mobile.responsive

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

/**
 * 响应式需要一个父级做为布局容器，来配合子级元素来实现变化效果
 * 原理就是在不同屏幕下，通过媒体查询来改变这个布局容器的大小，再改变里面子元素的排列方式和大小，从而实现不同屏幕下，看到不同的页面布局和样式变化。
 *
 * 平时我们的响应式尺寸划分
 * 超小屏幕（手机，小于768px）：设置宽度为100%
 * 小屏幕（平板，大于等于768px）：设置宽度为750px
 * 中等屏幕（桌面显示器，大于等于992px）：宽度设置为970px
 * 大屏幕（大桌面显示器，大于等于1200px）：宽度设置为1170px
 */
class ResponsivePage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, user-scalable=no, initial-scale=1.0," +
          "maximum-scale=1.0, minimum-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("响应式"),
        style(ResponsiveStyles.render[String]),
        style(ResponsiveMediaStyles.render[String]),
      ),
      body(
        // 响应式开发里面，首先需要一个布局容器
        div(cls := "container")(
          ul(List.fill(8)(li("导航栏")))
        )
      )
    )

end ResponsivePage
object ResponsivePage:
  object Text extends ResponsivePage(scalatags.Text)
end ResponsivePage

