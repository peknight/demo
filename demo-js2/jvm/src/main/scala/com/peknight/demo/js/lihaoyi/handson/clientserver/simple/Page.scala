package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import scalatags.Text.all._

object Page{

  val boot = "ClientServer.main(document.getElementById('contents'))"

  val skeleton =
    html(
      head(
        script(src := "/main.js"),
        link(
          rel := "stylesheet",
          href := "https://cdnjs.cloudflare.com/ajax/libs/pure/0.5.0/pure-min.css"
        )
      ),
      body(
        onload := boot,
        div(id := "contents")
      )
    )


}
