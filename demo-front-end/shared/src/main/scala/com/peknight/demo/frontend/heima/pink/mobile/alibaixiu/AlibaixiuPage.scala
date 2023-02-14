package com.peknight.demo.frontend.heima.pink.mobile.alibaixiu

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class AlibaixiuPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as inlineStyle, *}
  import bundle.tags2.{nav, section, style, title}

  private[this] val carouselId = "alibaixiu-carousel"

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        title("阿里百秀"),
        link(href := "/webjars/bootstrap/5.2.3/dist/css/bootstrap.min.css", rel := "stylesheet"),
        link(href := "/webjars/bootstrap-icons/1.10.3/font/bootstrap-icons.css", rel := "stylesheet"),
        style(raw(AlibaixiuMediaStyles.render[String])),
        style(raw(AlibaixiuStyles.render[String])),
        script(src := "/webjars/bootstrap/5.2.3/dist/js/bootstrap.bundle.min.js"),
      ),
      body(
        div(cls := "container")(
          div(cls := "row")(
            div(cls := "navbar navbar-expand-lg bg-light border rounded")(div(cls := "container-fluid")(
              a(cls := "navbar-brand", href := "#")("阿里百秀"),
              button(cls := "navbar-toggler", `type` := "button", attr("data-bs-toggle") := "collapse",
                attr("data-bs-target") := "#navbarSupportedContent", attr("aria-controls") := "navbarSupportedContent",
                attr("aria-expanded") := false, attr("aria-label") := "Toggle navigation")(
                span(cls := "navbar-toggler-icon")
              ),
              div(cls := "collapse navbar-collapse", id := "navbarSupportedContent")(
                ul(cls := "navbar-nav me-auto mb-2 mb-lg-0")(
                  li(cls := "nav-item")(a(cls := "nav-link active", attr("aria-current") := "page", href := "#")("生活馆")),
                  Seq("自然汇", "科技潮", "奇趣事", "美食杰").map(s => li(cls := "nav-item")(a(cls := "nav-link", href := "#")(s)))
                ),
                form(cls := "d-flex", role := "search")(
                  input(cls := "form-control me-2", `type` := "search", placeholder := "搜索", attr("aria-label") := "Search"),
                  button(cls := "btn btn-outline-success text-nowrap", `type` := "submit")("搜索")
                ),
                ul(cls := "navbar-nav mb-2 mb-lg-0 navbar-right")(
                  li(cls := "nav-item")(a(cls := "nav-link", href := "#", attr("data-bs-toggle") := "modal", attr("data-bs-target") := ".login")("登录")),
                  li(cls := "nav-item")(a(cls := "nav-link", href := "#")("注册")),
                ),
                div(cls := "modal fade login", tabindex := -1, attr("aria-labelledby") := "exampleModalLabel",
                  attr("aria-hidden") := true)(div(cls := "modal-dialog")(div(cls := "modal-content")(
                  div(cls := "modal-header")(
                    h1(cls := "modal-title fs-5", id := "exampleModalLabel")("登录信息"),
                    button(`type` := "button", cls := "btn-close", attr("data-bs-dismiss") := "modal", attr("aria-label") := "Close")
                  ),
                  div(cls := "modal-body")(
                    form(
                      div(cls := "mb-3")(
                        label(`for` := "exampleInputEmail1", cls := "form-label")("邮箱名登录"),
                        input(`type` := "email", cls := "form-control", id := "exampleInputEmail1", attr("aria-describedby") := "emailHelp"),
                        div(id := "emailHelp", cls := "form-text")("We'll never share your email with anyone else.")
                      ),
                      div(cls := "mb-3")(
                        label(`for` := "exampleInputPassword1", cls := "form-label")("密码"),
                        input(`type` := "password", cls := "form-control", id := "exampleInputPassword1"),
                      ),
                      div(cls := "mb-3 form-check")(
                        input(`type` := "checkbox", cls := "form-check-input", id := "exampleCheck1"),
                        label(cls := "form-check-label", `for` := "exampleCheck1")("记住用户名")
                      ),
                    )
                  ),
                  div(cls := "modal-footer")(
                    button(`type` := "button", cls := "btn btn-secondary", attr("data-bs-dismiss") := "modal")("关闭"),
                    button(`type` := "button", cls := "btn btn-primary")("登录")
                  )
                ))),
              )
            )),
          ),
          div(cls := "row")(
            header(cls := "col-md-2")(
              div(cls := "logo")(a(href := "#")(
                img(src := "/alibaixiu/images/logo.png", cls := "d-none d-sm-block"),
                span(cls := "d-block d-sm-none")("阿里百秀")
              )),
              div(cls := "nav")(ul(Seq(
                ("生活馆", "camera-fill"),
                ("自然汇", "image-fill"),
                ("科技潮", "phone-fill"),
                ("奇趣事", "gift-fill"),
                ("美食杰", "cup-hot-fill")
              ).map {
                case (text, icon) => li(a(href := "#", cls := s"bi bi-$icon")(text))
              })),
            ),
            tag("article")(cls := "col-md-7 article")(
              // 新闻
              div(cls := "news clearfix")(ul(
                // ("阿里百秀", "lg.png"),
                li(
                  div(id := carouselId, cls := "carousel slide", attr("data-bs-ride") := "carousel", attr("data-bs-interval") := 3000)(
                    div(cls :="carousel-inner")(
                      div(cls := "carousel-item active")(
                        img(src := "/jd/upload/banner.dpg", cls := "d-block w-100"),
                        div(cls := "carousel-caption d-none d-md-block")("阿里百秀1")
                      ),
                      for i <- 1 to 3 yield div(cls := "carousel-item")(
                        img(src := s"/jd/upload/banner$i.dpg", cls := "d-block w-100"),
                        div(cls := "carousel-caption d-none d-md-block")(s"阿里百秀${i + 1}")
                      ),
                    ),
                    button(cls := "carousel-control-prev", `type` := "button", attr("data-bs-target") := s"#$carouselId",
                      attr("data-bs-slide") := "prev")(
                      span(cls := "carousel-control-prev-icon", attr("aria-hidden") := "true"),
                      span(cls := "visually-hidden")("Previous"),
                    ),
                    button(cls := "carousel-control-next", `type` := "button", attr("data-bs-target") := s"#$carouselId",
                      attr("data-bs-slide") := "next")(
                      span(cls := "carousel-control-next-icon", attr("aria-hidden") := "true"),
                      span(cls := "visually-hidden")("Next"),
                    ),
                  )
                ),
                Seq(
                  ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "1.jpg"),
                  ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "2.jpg"),
                  ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "3.jpg"),
                  ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "4.jpg")
                ).map {
                  case (text, pic) => li(a(href := "#")(img(src := s"/alibaixiu/upload/${pic}"), p(text)))
                }
              )),
              // 发表
              div(cls := "publish")(List.fill(6)(
                div(cls := "row")(
                  div(cls := "col-md-9")(
                    h3("生活馆 关于指甲的10个健康知识 你知道几个？"),
                    p(cls := "text-muted d-none d-md-block")("alibaixiu 发布于2015-11-23"),
                    p(cls := "d-none d-md-block")("指甲是经常容易被人们忽略的身体部位，" +
                      "事实上从指甲的健康头部可以看出一个人的身体健康头部，快来看看10个隐藏在指甲里的知识吧！"),
                    p(cls := "text-muted")(
                      "阅读(2417)评论(1)赞18",
                      span(cls := "d-none d-md-block")("标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活")
                    )
                  ),
                  div(cls := "col-sm-3 pic d-none d-md-block")(img(src := "/alibaixiu/upload/3.jpg")),
                )
              )),
              nav(attr("aria-label") := "Page navigation example")(ul(cls := "pagination")(
                li(cls := "page-item")(a(cls := "page-link", href := "#", attr("aria-label") := "Previous")(
                  span(attr("aria-hidden") := "true")("«")
                )),
                li(cls := "page-item")(a(cls := "page-link", href := "#")(1)),
                li(cls := "page-item")(a(cls := "page-link", href := "#")(2)),
                li(cls := "page-item")(a(cls := "page-link", href := "#")(3)),
                li(cls := "page-item")(a(cls := "page-link", href := "#")(4)),
                li(cls := "page-item")(a(cls := "page-link", href := "#")(5)),
                li(cls := "page-item")(a(cls := "page-link", href := "#", attr("aria-label") := "Next")(
                  span(attr("aria-hidden") := "true")("»")
                )),
              ))
            ),
            tag("aside")(cls := "col-md-3")(
              a(href := "#", cls := "banner")(img(src := "/alibaixiu/upload/zgboke.jpg")),
              a(href := "#", cls := "hot")(
                span(cls := "btn btn-primary")("热搜"),
                h4(cls := "text-primary")("欢迎加入中国博客联盟"),
                p(cls := "text-muted")("这里收录国内各个领域的优秀博客，是一个全人工编辑的开放式博客联盟交流和展示平台......")
              ),
              div(inlineStyle := "margin-top: 10px")(
                ul(cls := "nav nav-tabs", id := "myTab", role := "tablist")(
                  li(cls := "nav-item", role := "presentation")(button(cls := "nav-link active", id := "home-tab",
                    attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#home-tab-pane", `type` := "button", role := "tab",
                    attr("aria-controls") := "home-tab-pane", attr("aria-selected") := "true")("搜索")),
                  li(cls := "nav-item", role := "presentation")(button(cls := "nav-link", id := "profile-tab",
                    attr("data-bs-toggle") := "tab", attr("data-bs-target") := "#profile-tab-pane", `type` := "button",
                    role := "tab", attr("aria-controls") := "profile-tab-pane", attr("aria-selected") := "false")("排名")),
                ),
                div(cls := "tab-content", id := "myTabContent")(
                  div(cls := "tab-pane fade show active", id := "home-tab-pane", role := "tabpanel",
                    attr("aria-labelledby") := "home-tab", tabindex := "0")("热搜的相关内容"),
                  div(cls := "tab-pane fade", id := "profile-tab-pane", role := "tabpanel",
                    attr("aria-labelledby") := "profile-tab", tabindex := "0")("排名的相关内容"),
                )
              )
            ),
          ),
        ),
      )
    )

end AlibaixiuPage
object AlibaixiuPage:
  object Text extends AlibaixiuPage(scalatags.Text)
end AlibaixiuPage

