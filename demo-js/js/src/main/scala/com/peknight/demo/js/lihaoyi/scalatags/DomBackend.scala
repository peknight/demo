package com.peknight.demo.js.lihaoyi.scalatags

import org.scalajs.dom.html.Paragraph
import scalatags.JsDom.all.*

object DomBackend:

  def domBackend(): Unit =
    val elem1 = div.render
    assert(elem1.children.length == 0)
    elem1.appendChild(p(1, "wtf", "bbq").render)
    assert(elem1.children.length == 1)
    val pElem = elem1.children(0).asInstanceOf[Paragraph]
    assert(pElem.childNodes.length == 3)
    assert(pElem.textContent == "1wtfbbq")

    var count = 0
    val elem2 = div(
      onclick := {() => count += 1},
      tabindex := 1
    ).render
    assert(count == 0)
    elem2.onclick(null)
    assert(count == 1)

    val labelElem = label("Default").render
    val inputElem = input(`type` := "text", onfocus := { () => labelElem.textContent = ""}).render
    val box = div(inputElem, labelElem).render
    assert(labelElem.textContent == "Default")
    inputElem.onfocus(null)
    assert(labelElem.textContent == "")

