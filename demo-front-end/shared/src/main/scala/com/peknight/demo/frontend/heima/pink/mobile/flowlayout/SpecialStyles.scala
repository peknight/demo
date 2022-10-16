package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * css3盒子模型
 * -webkit-box-sizing: border-box
 * 点击高亮我们需要清除 设置为transparent
 * -webkit-tap-highlight-color: transparent
 * 移动端浏览器默认的外观在iOS上加上这个属性才能给按钮和转入框自定义样式
 * -webkit-appearance: none
 * 禁用长按页面时的弹出菜单
 * img,a { -webkit-touch-callout: none; }
 */
object SpecialStyles extends StyleSheet.Standalone:
  import dsl.*

  ".demo-1 a" - (
    Attr.real("-webkit-tap-highlight-color") := "transparent",
    Attr.real("-webkit-touch-callout") := "none",
  )

  ".demo-2 input" - (
    Attr.real("-webkit-appearance") := "none"
  )

