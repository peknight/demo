package com.peknight.demo.oauth2.page

import org.http4s.Uri
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalacss.internal.ValueT
import scalacss.internal.ValueT.Color
import scalatags.generic.Bundle
import scalatags.text.Builder

class OAuthPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{style as _, title as _, *}
  import bundle.tags2.{nav, style, title}

  protected[oauth2] def jumbotron(pageName: String, navbarBrandLabelPosix: String,
                                navbarInverseBackgroundColor: ValueT[Color])
                               (jumbotron: Modifier*): Frag =
    skeleton(pageName, navbarBrandLabelPosix, navbarInverseBackgroundColor)(div(cls := "bg-body-tertiary p-5 rounded")(jumbotron))(None)

  protected[oauth2] def skeleton(pageName: String, navbarBrandLabelPosix: String,
                               navbarInverseBackgroundColor: ValueT[Color])
                              (main: Modifier*)(scriptSrc: Option[Uri]): Frag =
    html(lang := "en")(
      head(
        meta(charset := "utf-8"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        title(s"OAuth in Action: OAuth $pageName"),
        // link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"),
        link(rel := "stylesheet", href := "/webjars/bootstrap/5.3.1/dist/css/bootstrap.min.css"),
        style(OAuthStyles(navbarInverseBackgroundColor).render[String]),
        // script(src := "https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"),
        script (src := "/webjars/html5shiv/3.7.2/dist/html5shiv.min.js"),
        // script(src := "https://oss.maxcdn.com/respond/1.4.2/respond.min.js")
        script(src := "/webjars/respond.js/1.4.2/dest/respond.min.js")
      ),
      body(
        nav(cls := "navbar navbar-inverse fixed-top")(
          div(cls := "container")(
            div(cls := "navbar-header")(
              a(cls := "navbar-brand text-white-50", href := "/")(
                "OAuth in Action: ", span(cls := s"badge bg-$navbarBrandLabelPosix")(s"OAuth $pageName")
              )
            )
          )
        ),
        div(cls := "container")(
          main
        ),
        // script(src := "https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"),
        script(src := "/webjars/jquery/3.7.1/jquery.min.js"),
        // script(src := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"),
        script(src := "/webjars/bootstrap/5.3.1/dist/js/bootstrap.min.js"),
        scriptSrc.map(uri => script(src := uri.toString)).getOrElse("")
      )
    )

