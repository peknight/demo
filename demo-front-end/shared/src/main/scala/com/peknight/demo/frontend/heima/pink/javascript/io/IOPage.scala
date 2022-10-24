package com.peknight.demo.frontend.heima.pink.javascript.io

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class IOPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val io: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("IO"),
      ),
      body(
        "Hello World",
        script(`type` := "text/javascript", src := "/main.js"),
        script("promptDemo()"),
      )
    )

end IOPage
object IOPage:
  object Text extends IOPage(scalatags.Text)
end IOPage

