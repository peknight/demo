package com.peknight.demo.js.tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext.*
import utest.*

object TutorialTest extends TestSuite:
  def tests = Tests {

    TutorialApp.setupUI()

    test("HelloWorld") {
      // `querySelectorAll` to find all the `<p>` elements in the document, and `count` those whose `textContent` is the
      // `"Hello World"`. The `count` method is part of the Scala collections API, and is provided on DOM `NodeList`s
      // by the `import org.scalajs.dom.ext._`
      assert(document.querySelectorAll("p").count(_.textContent == "Hello World") == 1)
    }

    test("ButtonClick") {
      def messageCount = document.querySelectorAll("p").count(_.textContent == "You clicked the button!")
      val button = document.querySelector("button").asInstanceOf[dom.html.Button]
      assert(button != null && button.textContent == "Click me from js!")
      assert(messageCount == 0)
      for c <- 1 to 5 do
        button.click()
        assert(messageCount == c)
    }
  }
