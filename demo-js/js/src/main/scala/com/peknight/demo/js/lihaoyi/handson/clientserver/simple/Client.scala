package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scalatags.JsDom.all._

import scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.html

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


@JSExportTopLevel("ClientServer")
object Client {
  @JSExport
  def main(container: html.Div) = {
    val inputBox = input.render
    val outputBox = ul.render
    def update() = Ajax.post("/ajax/list", inputBox.value).foreach{xhr =>
      implicit val reader: upickle.default.Reader[FileData] = upickle.default.macroR
      val data = upickle.default.read[Seq[FileData]](xhr.responseText)
      outputBox.innerHTML = ""
      for (FileData(name, size) <- data) {
        outputBox.appendChild(
          li(
            b(name), "-", size, "bytes"
          ).render
        )
      }
    }
    inputBox.onkeyup = (e: dom.Event) => update()
    update()
    container.appendChild(
      div(
        h1("File Search"),
        inputBox,
        outputBox
      ).render
    )
  }
}
