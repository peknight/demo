package com.peknight.demo.frontend.heima.pink.javascript.jquery

import scalatags.generic.Bundle

class BootstrapPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{style as inlineStyle, title as inlineTitle, *}
  import bundle.tags2.{nav, section, style, title}

  override def links: Seq[String] = Seq("/webjars/bootstrap/5.2.2/dist/css/bootstrap.min.css")
  override def scripts: Seq[String] = Seq("/webjars/bootstrap/5.2.2/dist/js/bootstrap.bundle.min.js")
  override def headTitle: String = "Bootstrap"
  override def javaScriptMethod: Option[String] = Some("bootstrap")
  override def bodyFrag = div(cls := "container")(
    div(cls := "btn-group")(
      button(`type` := "button", cls := "btn btn-outline-secondary dropdown-toggle", `type` := "button",
        attr("data-bs-toggle") := "dropdown", attr("aria-expanded") := false)("Dropdown"),
      ul(cls := "dropdown-menu")(
        Seq("Action", "Another action", "Something else here").map(s => li(a(cls := "dropdown-item", href := "#")(s))),
        li(hr(cls := "dropdown-divider")),
        li(a(cls := "dropdown-item", href := "#")("Separated link"))
      )
    ),
    div(cls := "navbar navbar-expand-lg bg-light")(div(cls := "container-fluid")(
      a(cls := "navbar-brand", href := "#")("Navbar"),
      button(cls := "navbar-toggler", `type` := "button", attr("data-bs-toggle") := "collapse",
        attr("data-bs-target") := "#navbarSupportedContent", attr("aria-controls") := "navbarSupportedContent",
        attr("aria-expanded") := false, attr("aria-label") := "Toggle navigation")(
        span(cls := "navbar-toggler-icon")
      ),
      div(cls := "collapse navbar-collapse", id := "navbarSupportedContent")(
        ul(cls := "navbar-nav me-auto mb-2 mb-lg-0")(
          li(cls := "nav-item")(a(cls := "nav-link active", attr("aria-current") := "page", href := "#")("首页")),
          li(cls := "nav-item")(a(cls := "nav-link", href := "#")("公司简介")),
          li(cls := "nav-item dropdown")(
            a(cls := "nav-link dropdown-toggle", href := "#", role := "button", attr("data-bs-toggle") := "dropdown",
              attr("aria-expanded") := false)("Dropdown"),
            ul(cls := "dropdown-menu")(
              li(a(cls := "dropdown-item", href := "#")("Action")),
              li(a(cls := "dropdown-item", href := "#")("Another action")),
              li(hr(cls := "dropdown-divider")),
              li(a(cls := "dropdown-item", href := "#")("Something else here"))
            )
          ),
          li(cls := "nav-item")(a(cls := "nav-link disabled")("Disabled"))
        ),
        form(cls := "d-flex", role := "search")(
          input(cls := "form-control me-2", `type` := "search", placeholder := "Search", attr("aria-label") := "Search"),
          button(cls := "btn btn-outline-success", `type` := "submit")("Search")
        )
      )
    )),
    button(cls := "btn btn-primary", `type` := "button", attr("data-bs-toggle") := "modal",
      attr("data-bs-target") := "#exampleModalLg")("Large modal"),
    div(id := "exampleModalLg", cls := "modal fade", tabindex := -1, attr("aria-labelledby") := "exampleModelLgLabel",
      inlineStyle := "display: none;", attr("aria-hidden") := true)(
      div(cls := "modal-dialog modal-lg")(
        div(cls := "modal-content")(
          div(cls := "modal-header")(
            h1(id := "exampleModalLgLabel", cls := "modal-title fs-4")("Large modal"),
            button(cls := "btn-close", `type` := "button", attr("data-bs-dismiss") := "modal", attr("aria-label") := "Close")
          ),
          div(cls := "modal-body")(
            "..."
          )
        )
      )
    ),
    // button(cls := "myBtn", attr("data-bs-toggle") := "modal", attr("data-bs-target") := "#btn")("点击显示模态框"),
    button(cls := "myBtn", cls := "btn btn-success")("点击显示模态框"),
    div(cls := "modal fade", id := "btn", tabindex := -1, attr("aria-labelledby") := "exampleModalLabel",
      attr("aria-hidden") := true)(div(cls := "modal-dialog")(div(cls := "modal-content")(
      div(cls := "modal-header")(
        h1(cls := "modal-title fs-5", id := "exampleModalLabel")("Modal title"),
        button(`type` := "button", cls := "btn-close", attr("data-bs-dismiss") := "modal", attr("aria-label") := "Close")
      ),
      div(cls := "modal-body")("..."),
      div(cls := "modal-footer")(
        button(`type` := "button", cls := "btn btn-secondary", attr("data-bs-dismiss") := "modal")("Close"),
        button(`type` := "button", cls := "btn btn-primary")("Save changes")
      )
    ))),
    ul(cls := "nav nav-tabs", id := "myTab", role := "tablist")(
      li(cls := "nav-item", role := "presentation")(button(cls := "nav-link active", id := "home-tab",
        attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#home-tab-pane", `type` := "button", role := "tab",
        attr("aria-controls") := "home-tab-pane", attr("aria-selected") := "true")("手机")),
      li(cls := "nav-item", role := "presentation")(button(cls := "nav-link", id := "profile-tab",
        attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#profile-tab-pane", `type` := "button",
        role := "tab", attr("aria-controls") := "profile-tab-pane", attr("aria-selected") := "false")("电视")),
      li(cls := "nav-item", role := "presentation")(button(cls := "nav-link", id := "contact-tab",
        attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#contact-tab-pane", `type` := "button",
        role := "tab", attr("aria-controls") := "contact-tab-pane", attr("aria-selected") := "false")("Contact")),
      li(cls := "nav-item", role := "presentation")(button(cls := "nav-link", id := "disabled-tab",
        attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#disabled-tab-pane", `type` := "button",
        role := "tab", attr("aria-controls") := "disabled-tab-pane", attr("aria-selected") := "false", disabled := true
      )("Disabled"))
    ),
    div(cls := "tab-content", id := "myTabContent")(
      div(cls := "tab-pane fade show active", id := "home-tab-pane", role := "tabpanel",
        attr("aria-labelledby") := "home-tab", tabindex := "0")("手机相关内容"),
      div(cls := "tab-pane fade", id := "profile-tab-pane", role := "tabpanel",
        attr("aria-labelledby") := "profile-tab", tabindex := "0")("电视相关内容"),
      div(cls := "tab-pane fade", id := "contact-tab-pane", role := "tabpanel",
        attr("aria-labelledby") := "contact-tab", tabindex := "0")("..."),
      div(cls := "tab-pane fade", id := "disabled-tab-pane", role := "tabpanel",
        attr("aria-labelledby") := "disabled-tab", tabindex := "0")("...")
    )

  )

end BootstrapPage
object BootstrapPage:
  object Text extends BootstrapPage(scalatags.Text)
end BootstrapPage
