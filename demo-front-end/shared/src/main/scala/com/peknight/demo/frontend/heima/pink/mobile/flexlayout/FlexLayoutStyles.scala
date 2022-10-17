package com.peknight.demo.frontend.heima.pink.mobile.flexlayout

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * 布局原理
 * flex是flexible Box的缩写，意为"弹性布局"，用来为盒状模型提供最大的灵活性，任何一个容器都可以指定为flex布局。
 *
 * - 当我们为父盒子设为flex布局以后，子元素的float、clear和vertical-align属性将失效。
 * - 伸缩布局 = 弹性布局 = 伸缩盒布局 = 弹性盒布局 = flex布局
 *
 * 通过给父盒子添加flex模型，来控制子盒子的位置和排列方式
 *
 * 以下6个属性是对父元素设置的
 *
 * - flex-direction: 设置主轴的方向
 * - justify-content: 设置主轴上的子元素排列方式
 * - flex-wrap: 设置子元素是否换行
 * - align-content: 设置侧轴上的子元素的排列方式（多行）
 * - align-items: 设置侧轴上的子元素排列方式（单行）
 * - flex-flow: 复合属性，相当于同时设置了flex-direction和flex-wrap
 *
 * 以下是对子元素设置的
 *
 * - flex 属性定义子项目分配 剩余空间 ，用flex来表示占多少份数 flext: <number>;
 * - align-self 控制子项自己在侧轴上的排列方式 允许单个项目与其他项目不一样的对齐方式，可覆盖align-items属性。
 *   默认值为auto，表示继承父元素的align-items属性，如果没有父元素，则等同于stretch
 * - order 属性定义项目的排列顺序 数值越小，排列越靠前，默认为0。注意和z-index不一样
 */
object FlexLayoutStyles extends StyleSheet.Standalone:
  import dsl.*

  ".flex-1" - (
    display.flex,
    width(80.%%),
    height(300.px),
    backgroundColor.skyblue,
    // justifyContent.spaceAround,
    border(1.px, solid, black),
  )

  ".flex-1 span" - (
    // width(150.px),
    height(100.px),
    backgroundColor.green,
    marginRight(5.px),
    flex := "1",
  )

  ".flex-2" - (
    // 给父级添加flex属性
    display.flex,
    width(800.px),
    height(300.px),
    backgroundColor.skyblue,
    /*
     * 默认的主轴是x轴：行row，那么y轴就是侧轴喽
     * 我们的元素是跟着主轴来排列的
     */
    // flexDirection.row,
    // 简单了解 翻转
    // flexDirection.rowReverse / columnReverse
    // 我们可以把主轴设置为 y轴，那么x轴就成了侧轴
    flexDirection.column,
    border(1.px, solid, black),
  )

  ".flex-2 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
  )

  /**
   * justify-content
   *
   * justify-content属性定义了项目在主轴上的对齐方式
   * 注意：使用这个属性之前一定要确定好主轴是哪个
   * - flex-start 默认值 从头部开始，如果主轴是x轴，则从左到右
   * - flex-end 从尾部开始排列
   * - center 在主轴居中对齐（如果主轴是x轴则水平居中）
   * - space-around 平分剩余空间
   * - space-between 先两边贴边 再平分剩余空间（重要）
   */
  ".flex-3" - (
    display.flex,
    width(800.px),
    height(300.px),
    backgroundColor.skyblue,
    justifyContent.spaceBetween,
    border(1.px, solid, black),
  )

  ".flex-3 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
  )

  ".flex-4" - (
    display.flex,
    width(800.px),
    height(400.px),
    backgroundColor.skyblue,
    flexDirection.column,
    justifyContent.center,
    border(1.px, solid, black),
  )

  ".flex-4 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
  )

  ".flex-5" - (
    display.flex,
    width(600.px),
    height(400.px),
    backgroundColor.skyblue,
    // flex布局中，默认的子元素是不换行的，如果装不开会缩小子元素的宽度，放到父元素里面
    // nowrap是默认值
    flexWrap.nowrap,
    border(1.px, solid, black),
  )

  ".flex-5 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
    color.white,
    margin(10.px),
  )

  ".flex-6" - (
    display.flex,
    width(600.px),
    height(400.px),
    backgroundColor.skyblue,
    flexWrap.wrap,
    border(1.px, solid, black),
  )

  ".flex-6 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
    color.white,
    margin(10.px),
  )

  ".flex-7" - (
    display.flex,
    width(800.px),
    height(400.px),
    backgroundColor.skyblue,
    // 默认的主轴是x轴
    justifyContent.center,
    // 我们需要一个侧轴居中，还有flex-start flex-end stretch
    alignItems.center,
    border(1.px, solid, black),
  )

  ".flex-7 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
    color.white,
    margin(10.px),
  )

  ".flex-8" - (
    display.flex,
    width(800.px),
    height(400.px),
    backgroundColor.skyblue,
    justifyContent.center,
    // 拉伸，但是子盒子不要给高度
    alignItems.stretch,
    border(1.px, solid, black),
  )

  ".flex-8 span" - (
    width(150.px),
    backgroundColor.green,
    color.white,
    margin(10.px),
  )

  /**
   * align-content 设置侧轴上的子元素的排列方式（多行） 在单行下是没有效果的
   *
   * 这里说的多行 单行 只看flex-wrap值，设置为wrap但数量不满一行的也算多行
   *
   * - normal
   * - flex-start 在侧轴的头部开始排列
   * - flex-end 在侧轴的尾部开始排列
   * - center 在侧轴中间显示
   * - space-around 子项在侧轴平分剩余空间
   * - space-between 子项在侧轴先分布在两头，再平分剩余空间
   * - stretch 设置子项元素高度平分父元素高度
   */
  ".flex-9" - (
    display.flex,
    width(800.px),
    height(400.px),
    backgroundColor.skyblue,
    // 换行
    flexWrap.wrap,
    // 因为有了换行，此时我们侧轴上控制子元素的对齐方式我们用align-content:
    alignContent.spaceBetween,
    border(1.px, solid, black),
  )

  ".flex-9 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
    color.white,
    margin(10.px),
  )

  ".flex-10" - (
    display.flex,
    width(600.px),
    height(300.px),
    backgroundColor.skyblue,
    // flexDirection.column,
    // flexWrap.wrap,
    // 把设置主轴方向和是否换行（换列）简写
    flexFlow := "column wrap",
    border(1.px, solid, black),
  )

  ".flex-10 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.green,
  )

  ".flex-11" - (
    display.flex,
    width(60.%%),
    height(150.px),
    backgroundColor.pink,
  )

  ".flex-11 div".nthOfType(1) - (
    width(100.px),
    height(150.px),
    backgroundColor.red,
  )

  ".flex-11 div".nthOfType(2) - (
    flex := "1",
    backgroundColor.green,
  )

  ".flex-11 div".nthOfType(3) - (
    width(100.px),
    height(150.px),
    backgroundColor.blue,
  )

  ".flex-12" - (
    display.flex,
    width(60.%%),
    height(150.px),
    backgroundColor.pink,
  )

  ".flex-12 span" - (
    flex := "1",
    &.nthOfType(2) - (flex := "2", backgroundColor.purple),
  )

  ".flex-13" - (
    display.flex,
    width(80.%%),
    height(300.px),
    backgroundColor.pink,
    // 让三个子盒子沿着侧轴底侧对齐
    // alignItems.flexEnd,
  )

  ".flex-13 span" - (
    width(150.px),
    height(100.px),
    backgroundColor.purple,
    marginRight(5.px),
    &.nthOfType(2) - (
      order(-1),
    ),
    &.nthOfType(3) - (
      // 只让3号盒子下来
      alignSelf.flexEnd,
    ),
  )