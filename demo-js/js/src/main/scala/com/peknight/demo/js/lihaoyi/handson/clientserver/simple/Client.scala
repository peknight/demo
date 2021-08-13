package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import autowire._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html
import scalatags.JsDom.all._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


@JSExportTopLevel("ClientServer")
object Client {
  @JSExport
  def main(container: html.Div) = {
    val inputBox = input.render
    val outputBox = ul.render
    implicit val reader: upickle.default.Reader[FileData] = upickle.default.macroR
    def update() = Ajaxer[Api].list(inputBox.value).call().foreach{ data =>
//    def update() = Ajax.post("/ajax/list", inputBox.value).foreach{xhr =>
//      val data = upickle.default.read[Seq[FileData]](xhr.responseText)
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
