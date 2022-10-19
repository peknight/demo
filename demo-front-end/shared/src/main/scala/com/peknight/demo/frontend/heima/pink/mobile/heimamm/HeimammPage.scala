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
        link(rel := "stylesheet", href := "/css/swiper.min.css"),
        link(rel := "stylesheet", href := "/css/heimamm-media.css"),
        link(rel := "stylesheet", href := "/css/heimamm.css"),
      ),
      body(
        // 头部区域
        header(cls := "header")("黑马面面"),
        nav(cls := "nav")(
          Seq("HR面试", "笔试", "技术面试", "模拟面试", "面试技巧", "薪资查询").map(s => a(href := "#", cls := "item")(s))
        ),
        script(src := "/js/flexible.js"),
        script(src := "/js/swiper.min.js"),
        script(raw(
          """
            |// 第一个函数里面是就业指导轮播图
            |(function () {
            |  var swiper = new Swiper(".get_job_fo", {
            |    // 能够显示的slider的个数
            |    slidesPerView: 2,
            |    // 每个slide之间的距离
            |    spaceBetween: 30,
            |    centeredSlides: true,
            |    loop: true,
            |    // 添加左右箭头
            |    navigation: {
            |      nextEl: ".swiper-button-next",
            |      prevEl: ".swiper-button-prev",
            |    },
            |  });
            |})();
            |// 第二个函数的轮播图
            |(function() {
            |  // 如果有多个轮播图最好修改证 swiper-container
            |  var swiper = new Swiper(".study_fo", {
            |    // 我们可以看到的是2个半
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

