package com.peknight.demo.frontend.heima.pink.pinyougou.page

import com.peknight.demo.frontend.heima.pink.pinyougou.style.{BaseStyles, PinyougouStyles}
import org.http4s.Uri
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalatags.generic.Bundle
import scalatags.text.Builder

class PinyougouPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{style as _, title as _, *}
  import bundle.tags2.{nav, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        title("品优购 - 优质！优质！"),
        link(rel := "shortcut icon", href := "favicon.ico"),
        // link(rel := "stylesheet", href := "css/base.css"),
        style(BaseStyles.render[String]),
        style(PinyougouStyles.render[String])
      ),
      body(
        h1("Hello World")
      )
    )
end PinyougouPage

object PinyougouPage:
  object Text extends PinyougouPage(scalatags.Text)
end PinyougouPage

