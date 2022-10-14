package com.peknight.demo.frontend.page

import scalacss.ProdDefaults.*
import scalacss.internal.mutable.StyleSheet
import scalatags.generic.Bundle

class BasePage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  protected def simplePage(headTitle: String)(styleSheets: Modifier*)(bodyFrag: Frag*): Frag =
    html(lang := "zh-CN")(
      head(
        metaFrag,
        title(headTitle),
        styleSheets
      ),
      body(bodyFrag)
    )

  protected val metaFrag: Modifier =
    modifier(
      meta(charset := "UTF-8"),
      meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
      meta(httpEquiv := "X-UA-Compatible", content := "ie=edge")
    )

end BasePage