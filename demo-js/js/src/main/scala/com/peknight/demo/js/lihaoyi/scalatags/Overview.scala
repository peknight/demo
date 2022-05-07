package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*

object Overview:
  val page = html(
    head(
      script(src := "..."),
      script("alert('Hello World')")
    ),
    body(
      div(
        h1(id := "title", "This is a title"),
        p("This is a big paragraph of text")
      )
    )
  )

