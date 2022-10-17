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
