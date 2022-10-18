package com.peknight.demo.frontend.heima.pink.mobile.media

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * mediatype 查询类型
 *   将不同的终端设备划分成不同的类型，称为媒体类型
 *   all - 用于所有设备
 *   print - 用于打印机和打印预览
 *   screen - 用于电脑屏幕，平板电脑，智能手机等
 *
 * 媒体特性
 *   每种媒体类型都具体各自不同的特性，根据不同媒体类型的媒体特性设置不同的展示风格，我们暂且了解三个。注意他们要加小括号包含
 *   width - 定义输出设备中页面可见区域的宽度
 *   min-width - 定义输出设备中页面最小可见区域宽度
 *   max-width - 定义输出设备中页面最大可风区域宽度
 *
 * 媒体查询一般按照从大到小 或者 从小到大的顺序来
 */
object Demo1MediaStyles extends StyleSheet.Inline:
  import dsl.*

  style(
    // 在我们屏幕上 并且最大的宽度是539像素 设置我们想要的样式
    // 小于540 变为蓝色 px不能省略
    media.screen.maxWidth(539.px) (
      unsafeRoot("body")(
        backgroundColor.blue,
      ),
    ),
    // 大于等于540 小于970 变为绿色 下面大于等于970的情况会覆盖掉冲突的部分，这里的maxWidth(969.px)可以省略
    // media.screen.minWidth(540.px).maxWidth(969.px) (
    media.screen.minWidth(540.px) (
      unsafeRoot("body")(
        backgroundColor.green,
      ),
    ),
    // 大于等于970 变为红色
    media.screen.minWidth(970.px) (
      unsafeRoot("body")(
        backgroundColor.red,
      ),
    ),
  )
