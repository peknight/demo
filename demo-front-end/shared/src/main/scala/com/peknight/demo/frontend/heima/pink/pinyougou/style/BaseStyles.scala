package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object BaseStyles extends StyleSheet.Standalone:

  import dsl.*

  // 清除元素默认的内外边距
  "*" - (
    margin.`0`,
    padding.`0`,
    // css3盒子模型
    boxSizing.borderBox
  )

  // 让所有斜体 不倾斜
  "em, i" - fontStyle.normal

  // 去掉列表前面的小点
  "li" - style(listStyle := "none")

  // 图片没有边框 去掉图片底侧的空白缝隙
  "img" - (border.`0`, verticalAlign.middle)

  // 让button按钮变成小手
  "button" - cursor.pointer

  //取消链接的下划线
  "a" - style(color(c"#666"), textDecoration := "none")

  "a".hover - style(color(c"#e33333"))

  "button, input" - style(
    fontFamily :=! """'Microsoft YaHei', 'Heiti SC', tahoma, arial, 'Hiragino Sans GB', \\5B8B\4F53, sans-serif""",
    // 默认有灰色边框我们需要手动去掉
    border.`0`,
    // 去掉蓝色外边框
    outline.none
  )

  "body" - (
    backgroundColor(c"#fff"),
    font := """12px/1.5 'Microsoft YaHei', 'Heiti SC', tahoma, arial, 'Hiragino Sans GB', \\5B8B\4F53, sans-serif""",
    color(c"#666")
  )

  ".hide, .none" - display.none

  // 清除浮动
  ".clearfix".after - style(
    visibility.hidden,
    clear.both,
    display.block,
    content :=! "'.'",
    height.`0`
  )
