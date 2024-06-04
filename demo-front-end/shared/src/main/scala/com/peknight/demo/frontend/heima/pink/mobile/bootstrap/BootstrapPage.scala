package com.peknight.demo.frontend.heima.pink.mobile.bootstrap

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class BootstrapPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = skeleton("Bootstrap 101 Template")(style(raw(BootstrapStyles.render[String])))(
    button(`type` := "button", cls := "btn btn-danger")("登录"),
    span(cls := "bi bi-search"),
    // tag("svg")(xmlns := "http://www.w3.org/2000/svg", width := "16", height := "16", attr("fill") := "currentColor",
    //   cls :="bi bi-search", attr("viewBox") := "0 0 16 16")(
    //   tag("path")(attr("d") := "M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.853.85a1 " +
    //     "1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z")
    // )
    nav(cls := "navbar navbar-expand-sm navbar-dark bg-dark", attr("aria-label") := "Third navbar example")(
      div(cls := "container-fluid")(
        a(cls := "navbar-brand", href := "#")("Expand at sm"),
        button(cls := "navbar-toggler", `type` := "button", attr("data-bs-toggle") := "collapse",
          attr("data-bs-target") := "#navbarsExample03", attr("aria-controls") := "navbarsExample03",
          attr("aria-expanded") := "false", attr("aria-label") := "Toggle navigation")(
          span(cls := "navbar-toggler-icon")
        ),
        div(cls := "collapse navbar-collapse", id := "navbarsExample03")(
          ul(cls := "navbar-nav me-auto mb-2 mb-sm-0")(
            li(cls := "nav-item")(a(cls := "nav-link active", attr("aria-current") := "page", href := "#")("Home")),
            li(cls := "nav-item")(a(cls := "nav-link", href := "#")("Link")),
            li(cls := "nav-item")(a(cls := "nav-link disabled")("Disabled")),
            li(cls := "nav-item dropdown")(
              a(cls := "nav-link dropdown-toggle", href := "#", attr("data-bs-toggle") := "dropdown",
                attr("aria-expanded") := "false")("Dropdown"),
              ul(cls := "dropdown-menu")(
                li(a(cls := "dropdown-item", href := "#")("Action")),
                li(a(cls := "dropdown-item", href := "#")("Another action")),
                li(a(cls := "dropdown-item", href := "#")("Something else here")),
              ),
            ),
          ),
          form(role := "search")(
            input(cls := "form-control", `type` := "search", placeholder := "Search", attr("aria-label") := "Search")
          )
        )
      )
    ),
    button(`type` := "button", cls := "btn btn-success")("Success"),
    div(cls := "btn btn-success login")("登录"),
    div(cls := "btn btn-danger")("未成功"),
    // 不同档位划分不同的宽度
    div(cls := "container")(123),
    // 永远宽度100%
    div(cls := "container-fluid")(123),
    div(cls := "container")(
      div(cls := "row")(for i <- 1 to 4 yield div(cls := "col-lg-3 col-md-4 col-sm-6 col-12")(i)),
      // 如果孩子的份数相加等于12 则孩子能占满整个的container的宽度
      div(cls := "row")(
        div(cls := "col-lg-6")(1),
        div(cls := "col-lg-2")(2),
        div(cls := "col-lg-2")(3),
        div(cls := "col-lg-2")(4),
      ),
      // 如果孩子的份数相加小于12 则占不满整个container的宽度 会有空白
      div(cls := "row")(
        div(cls := "col-lg-6")(1),
        div(cls := "col-lg-2")(2),
        div(cls := "col-lg-2")(3),
        div(cls := "col-lg-1")(4),
      ),
      // 如果孩子的份数相加大于12 则多余的那一列会另起一行显示
      div(cls := "row")(
        div(cls := "col-lg-6")(1),
        div(cls := "col-lg-2")(2),
        div(cls := "col-lg-2")(3),
        div(cls := "col-lg-3")(4),
      ),
      div(cls := "row")(
        div(cls := "col-md-4")(
          // 列嵌套最好加一个行 row 这样可以取消父元素的padding值，而且高度自动和父级一样高
          div(cls := "row")(
            div(cls := "col-md-4")("a"),
            div(cls := "col-md-8")("b"),
          )
        ),
        div(cls := "col-md-4")(2),
        div(cls := "col-md-4")(3),
      ),
      div(cls := "row")(
        div(cls := "col-md-4")("左侧"),
        div(cls := "col-md-4 offset-md-4")("右侧"),
      ),
      div(cls := "row")(
        // 如果只有一个盒子
        div(cls := "col-md-8 offset-md-2")("中间盒子"),
      ),
      div(cls := "row")(
        div(cls := "col-md-4")("左侧"),
        div(cls := "col-md-8 order-first")("右侧"),
      ),
      div(cls := "row")(
        div(cls := "col-3")(span(cls := "d-none d-lg-block")("我会显示哦")),
        div(cls := "col-3")(2),
        div(cls := "col-3 d-md-none d-lg-block")("我会变魔术哦"),
        div(cls := "col-3")(4),
      ),
    )
  )
  def skeleton(headTitle: String)(styles: Modifier*)(modifiers: Modifier*): Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        link(href := "/webjars/bootstrap/5.3.3/dist/css/bootstrap.min.css", rel := "stylesheet"),
        link(href := "/webjars/bootstrap-icons/1.11.3/font/bootstrap-icons.css", rel := "stylesheet"),
        // link(rel := "stylesheet", href := "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"),
        // link(
        //   href := "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css",
        //   rel := "stylesheet",
        //   integrity := "sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi",
        //   crossorigin := "anonymous"
        // ),
        title(headTitle),
        styles,
      ),
      body(
        modifiers,
        script(src := "/bootstrap/js/bootstrap.bundle.min.js"),
      )
    )


end BootstrapPage
object BootstrapPage:
  object Text extends BootstrapPage(scalatags.Text)
end BootstrapPage

