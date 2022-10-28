package com.peknight.demo.frontend.heima.pink.mobile.alibaixiu

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class AlibaixiuPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        title("阿里百秀"),
        link(href := "/webjars/bootstrap/5.2.2/dist/css/bootstrap.min.css", rel := "stylesheet"),
        link(href := "/webjars/bootstrap-icons/1.9.1/font/bootstrap-icons.css", rel := "stylesheet"),
        style(raw(AlibaixiuMediaStyles.render[String])),
        style(raw(AlibaixiuStyles.render[String])),
      ),
      body(
        div(cls := "container")(
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
              div(cls := "news clearfix")(ul(Seq(
                ("阿里百秀", "lg.png"),
                ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "1.jpg"),
                ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "2.jpg"),
                ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "3.jpg"),
                ("奇了 成都一小区护卫长的像马云 市民纷纷请求合影", "4.jpg")
              ).map {
                case (text, pic) => li(a(href := "#")(img(src := s"/alibaixiu/upload/${pic}"), p(text)))
              })),
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
            ),
            tag("aside")(cls := "col-md-3")(
              a(href := "#", cls := "banner")(img(src := "/alibaixiu/upload/zgboke.jpg")),
              a(href := "#", cls := "hot")(
                span(cls := "btn btn-primary")("热搜"),
                h4(cls := "text-primary")("欢迎加入中国博客联盟"),
                p(cls := "text-muted")("这里收录国内各个领域的优秀博客，是一个全人工编辑的开放式博客联盟交流和展示平台......")
              ),
            ),
          ),
        ),
        script(src := "/webjars/bootstrap/5.2.2/dist/js/bootstrap.bundle.min.js"),
      )
    )

end AlibaixiuPage
object AlibaixiuPage:
  object Text extends AlibaixiuPage(scalatags.Text)
end AlibaixiuPage

