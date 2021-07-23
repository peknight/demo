package com.peknight.demo.js.tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel

object TutorialApp {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
      setupUI()
    })
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

  // Reacting on User Input
  // make this method callable as top-level function from JavaScript
  @JSExportTopLevel("addClickedMessage")
  def addClickedMessage(): Unit = {
    appendPar(document.body, "You clicked the button!")
  }

  // Setup the UI in Scala.js
  def setupUI(): Unit = {
    val button = document.createElement("button")
    button.textContent = "Click me from js!"
    button.addEventListener("click", { (e: dom.MouseEvent) =>
      addClickedMessage()
    })
    document.body.appendChild(button)
    appendPar(document.body, "Hello World")
  }
}
