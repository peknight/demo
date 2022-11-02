package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

abstract class JQueryPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  protected def links: Seq[String] = Seq()
  protected def styles: Seq[StyleSheet.Base] = Seq()
  protected def scripts: Seq[String] = Seq()
  protected def headTitle: String = "Document"
  protected def javaScriptMethod: Option[String] = None
  protected def bodyFrag: Frag = ""

  def index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        links.map(s => link(rel := "stylesheet", href := s)),
        styles.map(s => style(raw(s.render[String]))),
        script(`type` := "text/javascript", src := "/webjars/jquery/3.6.1/jquery.min.js"),
        script(`type` := "text/javascript", src := "/main.js"),
        scripts.map(s => script(src := s)),
        title(headTitle),
      ),
      body(
        bodyFrag,
        javaScriptMethod.map(m => script(s"$m()")),
      )
    )

end JQueryPage
