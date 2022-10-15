package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 物理像素 就是我们说的分辨率 iPhone8的物理像素是750
 * 在 iPhone8里面1px开发像素 = 2个物理像素
 * 开发像素在移动端并不总是与物理像素一一对应
 *
 * 我们需要一个50*50像素(css像素)的图片，直接放到我们的iphone8里面会放大2倍，100*100就会模糊
 * 我们采取的是放一个100*100图片，然后手动把这个图片缩小为50*50(css像素)
 * 我们准备的图片比我们实际需要的大小大2倍，这种方式就是2倍图
 */
object Image2XStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
  )

  ".demo-1" - (
    width(300.px),
    height(300.px),
    backgroundColor.pink,
  )

  ".demo-2 img".nthOfType(2) - (
    width(50.px),
    height(50.px),
  )

