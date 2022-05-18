package com.peknight.demo.oauth2.protectedresource

import scalatags.Text.all.{title as _, *}
import scalatags.Text.tags2.title

object ProtectedResourcePage:

  val skeleton = "<!DOCType html>" +
    html(lang := "en")(
      head(
        meta(charset := "utf-8"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        title("OAuth in Action: OAuth Protected Resource"),
        link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
      ),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        )
      )
    )