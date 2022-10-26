package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.heima.pink.javascript.domain.StudentScore
import org.scalajs.dom
import scalatags.JsDom.all.*

import scala.scalajs.js.annotation.JSExportTopLevel

object DynamicTableScript:

  type StudentScoreFrag[_] = Frag
  @JSExportTopLevel("dynamicTable")
  def dynamicTable(): Unit =
    val datas: Seq[StudentScore] = Seq(
      StudentScore("魏瓔珞", "JavaScript", 100),
      StudentScore("弘历", "JavaScript", 98),
      StudentScore("傅恒", "JavaScript", 99),
      StudentScore("明玉", "JavaScript", 88),
      StudentScore("大猪蹄子", "JavaScript", 0)
    )
    val tbodyElement = dom.document.querySelector("tbody")
    datas.foreach { studentScore =>
      val trElement = tr(
        (Tuple.fromProductTyped(studentScore).map[StudentScoreFrag] { [T] => (t: T) => t match
          case s: String => td(s).asInstanceOf[Frag]
          case i: Int => td(i).asInstanceOf[Frag]
        } :* td(a(href := "javascript:;")("删除"))).toList
      ).render
      tbodyElement.append(trElement)
      trElement.querySelector("a").asInstanceOf[dom.HTMLElement].onclick = _ =>
        tbodyElement.removeChild(trElement)
    }