package com.peknight.demo.tyrian.guide

import tyrian.*
import tyrian.Html.*
import tyrian.syntax.*

object RenderingHTMLApp:

  enum Msg:
    case Greet
  end Msg

  val myStyles = style(CSS.`font-family`("Arial, Helvetica, sans-serif"))

  val topLine = p(b(text("This is some HTML in bold.")))

  div(id := "my-container")(
    div(myStyles)(
      topLine,
      p("Hello, world!"),
      button(onClick(Msg.Greet))("Say hello!")
    )
  )

  span(`class` := "green-box")(
    p("This is some text.")
  )

  span(
    p("This is some text.")
  )

  p("some text")
  p(text("some text"))

  id := "my-container"
  p(style(CSS.`font-weight`("bold")))("Hello")

  Option(p("Show this!")).orEmpty

  val showIt = true
  if showIt then p("Show this!") else Empty

  // A 'canvas' tag
  tag("canvas")(id := "an-id")(Nil)
  // or
  Tag("canvas", List(id := "an-id"), Nil)

  // An attribute
  attribute("my-attribute", "its-value")

  // A property
  property("my-property", "its-value")

  // Styles
  style("width", "100px")
  styles("width" -> "100px", "height" -> "50px")

  // An event-type attribute
  onEvent("click", (evt: Tyrian.Event) => Msg.Greet)
end RenderingHTMLApp
