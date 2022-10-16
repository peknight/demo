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

  ".demo-3 div" - (
    width(500.px),
    height(500.px),
    border(2.px, solid, red),
    background := "url('/flowlayout/images/dog.jpg') no-repeat",
  )

  ".demo-3 .bg-size-1" - (
    backgroundSize := "500px 200px",
  )

  ".demo-3 .bg-size-2" - (
    // 只写一个参数 肯定是宽度，调试省略了，会等比例缩放
    backgroundSize := "500px",
  )

  ".demo-3 .bg-size-3" - (
    // 里面的单位可以跟% 相对于父盒子来说的
    backgroundSize := "50%",
  )

  ".demo-3 .cover-1" - (
    // cover要完全覆盖div盒子，可能有部分背景图片显示不全
    backgroundSize := "cover",
  )

  ".demo-3 .contain-1" - (
    // contain 高度和宽度等比例拉伸 当宽度或者高度铺满div盒子就不再进行拉伸了 可能有部分空白区域
    backgroundSize := "contain",
  )

  ".demo-3 .contain-2" - (
    width(100.px),
    height(100.px),
    backgroundSize := "contain",
  )

  ".demo-4 .apple-50" - (
    width(50.px),
    height(50.px),
    border(1.px, solid, red),
    background := "url('/flowlayout/images/apple50.jpg') no-repeat",
  )

  ".demo-4 .apple-100" - (
    width(50.px),
    height(50.px),
    border(1.px, solid, red),
    background := "url('/flowlayout/images/apple100.jpg') no-repeat",
    backgroundSize := "50px 50px",
  )
