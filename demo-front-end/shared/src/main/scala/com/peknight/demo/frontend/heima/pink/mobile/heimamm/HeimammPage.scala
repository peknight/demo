package com.peknight.demo.frontend.heima.pink.mobile.heimamm

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class HeimammPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "en")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        title("黑马面面"),
        link(rel := "stylesheet", href := "/css/normalize.css"),
        // 先引入css文件 放到自己css文件的上面
        link(rel := "stylesheet", href := "/webjars/swiper/8.3.2/swiper-bundle.min.css"),
        link(rel := "stylesheet", href := "/css/heimamm-media.css"),
        link(rel := "stylesheet", href := "/css/heimamm.css"),
      ),
      body(
        section(cls := "wrap")(
          // 头部区域
          header(cls := "header")("黑马面面"),
          nav(cls := "nav")(Seq("HR面试", "笔试", "技术面试", "模拟面试", "面试技巧", "薪资查询").zipWithIndex.map {
            case (s, index) => a(href := "#", cls := "item")(img(src := s"/heimamm/icons/icon${index + 1}.png"), span(s))
          }),
          // go模块
          section(cls := "go")(img(src := "/heimamm/images/go.png")),
        ),
        // 就业指导模块
        section(cls := "content")(
          div(cls := "con-hd")(
            h4(span(cls := "icon")(img(src := "/heimamm/icons/i2.png")), "就业指导"),
            a(href := "#", cls := "more")("更多>>"),
          ),
          div(cls := "get-job-focus")(
            div(cls := "swiper get-job-fo")(div(cls := "swiper-wrapper")(Seq("pic.png", "ldh.jpg", "3.jpg").map(s =>
              div(cls := "swiper-slide")(
                a(href := "#")(img(src := s"/heimamm/images/$s")),
                p("老师教你应对面试技巧")
              )
            ))),
            // 根据需求，这个代码放到container外面
            div(cls := "swiper-button-prev"),
            div(cls := "swiper-button-next"),
          ),
        ),

        // 充电学习模块
        section(cls := "content study-con")(
          div(cls := "con-hd")(
            h4(span(cls := "icon")(img(src := "/heimamm/icons/i2.png")), "充电学习"),
            a(href := "#", cls := "more")("更多>>"),
          ),
          // 学习模块轮播图
          div(cls := "study")(div(cls := "swiper study-fo")(
            div(cls := "swiper-wrapper")(Seq("pic1.png", "pic2.png", "pic1.png", "pic1.png").map(s =>
              div(cls := "swiper-slide")(
                img(src := s"/heimamm/images/$s"),
                h5("说低调英语，告别中式英语"),
                p("156人学习"),
              )
            ))
          ))
        ),

        footer(cls := "footer")(Seq(("首页", "home"), ("模拟面试", "ms"), ("技术面试", "net"), ("我的首页", "user")).map {
          case (text, pic) => a(href := "#", cls := "item")(img(src := s"/heimamm/icons/$pic.png"), p(text))
        }),
        script(src := "/webjars/amfe-flexible/2.2.1/index.min.js"),
        script(src := "/webjars/swiper/8.3.2/swiper-bundle.min.js"),
        script(raw(
          """
            |// 第一个函数里面是 就业指导轮播图
            |(function() {
            |  var swiper = new Swiper(".get-job-fo", {
            |    slidesPerView: 2,
            |    spaceBetween: 30,
            |    centeredSlides: true,
            |    loop: true,
            |    navigation: {
            |      nextEl: '.swiper-button-next',
            |      prevEl: '.swiper-button-prev',
            |    },
            |  });
            |})();
            |// 第二个函数的轮播图
            |(function() {
            |  var swiper = new Swiper(".study-fo", {
            |    slidesPerView: 2.2,
            |    spaceBetween: 20,
            |  });
            |})();
          """.stripMargin
        ))
      )
    )

end HeimammPage
object HeimammPage:
  object Text extends HeimammPage(scalatags.Text)
end HeimammPage

