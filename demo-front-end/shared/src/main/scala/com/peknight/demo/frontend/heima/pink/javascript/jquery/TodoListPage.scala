package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.TodoListPage.{MediaStyles, Styles}
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class TodoListPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles, MediaStyles)
  override def headTitle: String = "TodoList-最简单的待办事项列表"
  override def javaScriptMethod: Option[String] = Some("todoList")
  override def bodyFrag = frag(
    header(section(
    label(`for` := "title")("TodoList"),
    input(`type` := "text", id := "title", name := "title", placeholder := "添加Todo", required := true,
      autocomplete := "off")
    )),
    section(
      h2("正在进行", span(id := "todo-count")),
      ol(id := "todo-list", cls := "demo-box"),
      h2("已经完成", span(id := "done-count")),
      ul(id := "done-list")
    ),
    footer("Copyright © 2014 todolist.cn")
  )
end TodoListPage
object TodoListPage:
  object Text extends TodoListPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "body" - (margin.`0`, padding.`0`, fontSize(16.px), background := "#cdcdcd")
    "header" - (height(50.px), background := "rgba(47,47,47,0.98)")
    "section" - margin(`0`, auto)
    "label" - (
      float.left,
      width(100.px),
      lineHeight(50.px),
      color(c"#ddd"),
      fontSize(24.px),
      cursor.pointer,
      fontFamily :=! "'Helvetica Neue', Helvetica, Arial, sans-serif"
    )
    "header input" - (
      float.right,
      width(60.%%),
      height(24.px),
      marginTop(12.px),
      textIndent(10.px),
      borderRadius(5.px),
      boxShadow := "0 1px 0 rgba(255,255,255,0.24), 0 1px 6px rgba(0,0,0,0.45) inset",
      border.none
    )
    "input".focus - outlineWidth.`0`
    "h2" - position.relative
    "span" - (
      position.absolute,
      top(2.px),
      right(5.px),
      display.inlineBlock,
      padding(`0`, 5.px),
      height(20.px),
      borderRadius(20.px),
      background := "#e6e6fa",
      lineHeight(22.px),
      textAlign.center,
      color(c"#666"),
      fontSize(14.px)
    )
    "ol,ul" - (padding.`0`, listStyle := "none")
    "li input" - (
      position.absolute,
      top(2.px),
      left(10.px),
      width(22.px),
      height(22.px),
      cursor.pointer
    )
    "p" - margin.`0`
    "li p input" - (
      top(3.px),
      left(40.px),
      width(70.%%),
      height(20.px),
      lineHeight(14.px),
      textIndent(5.px),
      fontSize(14.px)
    )
    "li" - (
      height(32.px),
      lineHeight(32.px),
      background := "#fff",
      position.relative,
      marginBottom(10.px),
      padding(`0`, 45.px),
      borderRadius(3.px),
      borderLeft(5.px, solid, c"#629a9c"),
      boxShadow := "0 1px 2px rgba(0,0,0,0.07)"
    )
    "ol li" - cursor.move
    "ul li" - (borderLeft(5.px, solid, c"#999"), opacity(0.5))
    "li a" - (
      position.absolute,
      top(2.px),
      right(5.px),
      display.inlineBlock,
      width(14.px),
      height(12.px),
      borderRadius(14.px),
      border(6.px, double, c"#fff"),
      background := "#ccc",
      lineHeight(14.px),
      textAlign.center,
      color(c"#fff"),
      fontWeight.bold,
      fontSize(14.px),
      cursor.pointer
    )
    "footer" - (
      color(c"#666"),
      fontSize(14.px),
      textAlign.center
    )
    "footer a" - (
      color(c"#666"),
      textDecoration := "none",
      color(c"#999")
    )
  end Styles
  object MediaStyles extends StyleSheet.Inline:
    import dsl.*
    style(
      media.screen.maxDeviceWidth(620.px)(unsafeRoot("section")(width(96.%%), padding(`0`, 2.%%))),
      media.screen.minWidth(620.px)(unsafeRoot("section")(width(600.px), padding(`0`, 10.px)))
    )
end TodoListPage
