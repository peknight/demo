package com.peknight.demo.frontend.heima.pink.mobile.suning

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class SuningPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, user-scalable=no, initial-scale=1.0," +
          "maximum-scale=1.0, minimum-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("苏宁"),
        link(rel := "stylesheet", href := "/css/normalize.css"),
        link(rel := "stylesheet", href := "/css/common.css"),
        link(rel := "stylesheet", href := "/css/suning.css"),
      ),
      body(

      )
    )

end SuningPage
object SuningPage:
  object Text extends SuningPage(scalatags.Text)
end SuningPage

