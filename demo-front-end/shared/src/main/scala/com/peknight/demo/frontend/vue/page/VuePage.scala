package com.peknight.demo.frontend.vue.page

import scalacss.ProdDefaults.*
import scalacss.internal.mutable.StyleSheet
import scalatags.generic.Bundle

abstract class VuePage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  protected def links: Seq[String] = Seq()
  protected def styles: Seq[StyleSheet.Base] = Seq()
  protected def scripts: Seq[String] = Seq()
  protected def headTitle: String = "Document"
  protected def javaScriptMethod: Option[String] = None
  protected def bodyFrag: Frag = frag()

  def index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        links.map(s => link(rel := "stylesheet", href := s)),
        styles.map(s => style(raw(s.render[String]))),
        script(`type` := "text/javascript", src := "/webjars/vue/3.2.45/dist/vue.global.prod.js"),
        script(`type` := "text/javascript", src := "/main.js"),
        scripts.map(s => script(src := s)),
        title(headTitle),
      ),
      body(
        bodyFrag,
        javaScriptMethod.map(m => script(s"$m()")),
      )
    )

  def esm: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        links.map(s => link(rel := "stylesheet", href := s)),
        styles.map(s => style(raw(s.render[String]))),
        script(`type` := "importmap")(raw(
          """
            |{
            |  "imports": {
            |    "vue": "/webjars/vue/3.2.45/dist/vue.esm-browser.prod.js"
            |  }
            |}
          """.stripMargin
        )),
        scripts.map(s => script(src := s)),
        title(headTitle),
      ),
      body(
        bodyFrag,
        javaScriptMethod.map(m => script(`type` := "module")(
          s"""
             |import * as m from '/demo-vue.js'
             |m.$m()
          """.stripMargin
        )),
      )
    )
end VuePage
