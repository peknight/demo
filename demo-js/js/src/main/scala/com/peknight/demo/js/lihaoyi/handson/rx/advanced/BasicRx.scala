package com.peknight.demo.js.lihaoyi.handson.rx.advanced

import org.scalajs.dom
import org.scalajs.dom.html
import rx._
import scalatags.JsDom.all._

import scala.annotation.tailrec
import scala.language.implicitConversions
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

@JSExportTopLevel("BasicRxClient")
object BasicRx {

  @JSExport
  def main(container: html.Div) = {
    implicit val ctx: Ctx.Owner = Ctx.Owner.safe
    val txt = Var("")
    val numChars = Rx { txt().length }
    val numWords = Rx {
      txt().split(' ')
        .filter(_.length > 0)
        .length
    }
    val avgWordLength = Rx {
      txt().count(_ != ' ') * 1.0 / numWords()
    }

    val txtInput = textarea.render
    txtInput.onkeyup = (e: dom.Event) => {
      txt() = txtInput.value
    }

    container.appendChild(
      div(
        txtInput,
        ul(
          li("Chars: ", numChars),
          li("Words: ", numWords),
          li("Word Length: ", avgWordLength)
        )
      ).render
    )
  }

  @JSExport
  def main2(container: html.Div) = {
    implicit val ctx: Ctx.Owner = Ctx.Owner.safe
    val fruits = Seq(
      "Apple", "Apricot", "Banana", "Cherry",
      "Mango", "Mangosteen", "Mandarin",
      "Grape", "Grapefruit", "Guava"
    )
    val query = Var("")
    val txtInput = input.render
    txtInput.onkeyup = (e: dom.Event) => {
      query() = txtInput.value
    }

    val fragments = for (fruit <- fruits) yield Rx {
      val shown = fruit.toLowerCase.startsWith(query())
      if (shown) li(fruit)
      else li(display := "none")
    }

    container.appendChild(
      div(
        txtInput,
        ul(fragments)
      ).render
    )
  }

  /**
   * {@link https://github.com/Voltir/framework.rx/blob/master/src/main/scala/framework/Framework.scala}
   */
  implicit def rxFrag[T](n: Rx[T])(implicit f: T => Frag, ctx: Ctx.Owner): Frag = {
    @tailrec def clearChildren(node: dom.Node): Unit = {
      if (node.firstChild != null) {
        node.removeChild(node.firstChild)
        clearChildren(node)
      }
    }
    def fSafe: Frag = n match {
      case r: Rx.Dynamic[T] => r.toTry match {
        case Success(v) => v.render
        case Failure(e) => span(e.getMessage, backgroundColor := "red").render
      }
      case v: Var[T] => v.now.render
    }

    var last = fSafe.render
    val container = span(cls := "_rx")(last).render
    n.triggerLater {
      val newLast = fSafe.render
      clearChildren(container)
      container.appendChild(newLast)
      last = newLast
    }
    bindNode(container)
  }
}
