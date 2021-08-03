package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all._

import scala.scalajs.js.annotation.JSExportTopLevel

/**
 *
 * @author shuigen.rengang [rg249590@alibaba-inc.com]
 * @version $Id:ReRendering.java,v 0.1 2021-08-02 15:09 shuigen Exp $$
 */
object ReRendering {

  val box = input(
    `type` := "text",
    placeholder := "Type here!"
  ).render

  val listings = Seq(
    "Apple", "Apricot", "Banana", "Cherry",
    "Mango", "Mangosteen", "Mandarin",
    "Grape", "Grapefruit", "Guava"
  )

  def renderListings = ul(
    for {
      fruit <- listings if fruit.toLowerCase.startsWith(box.value.toLowerCase)
    } yield {
      val (first, last) = fruit.splitAt(box.value.length)
      li(
        span(
          backgroundColor := "yellow",
          first
        ),
        last
      )
    }
  ).render

  val output = div(renderListings).render

  box.onkeyup = (e: dom.Event) => {
    output.innerHTML = ""
    output.appendChild(renderListings)
  }

  @JSExportTopLevel("reRendering")
  def reRendering(target: html.Div): Unit = {
    target.appendChild(
      div(
        h1("Search Box!"),
        p(
          "Type here to filter " +
          "the list of things below!"
        ),
        div(box),
        output
      ).render
    )
  }
}
