package com.peknight.demo.frontend.heima.pink.css3.transform2d

import com.peknight.demo.frontend.page.BasePage
import scalatags.generic.Bundle

class Transform2DPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def transform2D: Frag = simplePage("2D Transform")(Transform2DStyles)(
    // 位移
    div(cls := "box")(
      div(cls := "trans-1")("trans-1"),
      div(cls := "trans-2")("trans-2"),
      div(cls := "trans-3")("trans-3"),
    ),
    // 居中
    div(cls := "box")(
      div(cls := "center-outer")(div(cls := "center-inner")("center-inner")),
      span("Translate对行内元素无效"),
    ),
    // 旋转
    div(cls := "box")(
      div(cls := "rotate-1")("rotate-1")
    ),
    // 画三角
    div(cls := "box")(div(cls := "triangle-1")),
    // 转换中心点
    div(cls := "box")(
      div(cls := "origin-1")("origin-1"),
      div(cls := "origin-2")("origin-2"),
    ),
    div(cls := "box")(
      div(cls := "case")("case"),
    ),
    div(cls := "box")(
      div(cls := "scale-1")("scale-1"),
      div(cls := "scale-2")("scale-2"),
      div(cls := "scale-3")("scale-3"),
    ),
  )

end Transform2DPage
object Transform2DPage:
  object Text extends Transform2DPage(scalatags.Text)
end Transform2DPage


