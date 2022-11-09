package com.peknight.demo.frontend.heima.pink.jquery

import com.peknight.demo.frontend.heima.pink.javascript.domain.TodoItem
import io.circe.generic.auto.*
// 这里用io.circe.parser.* 会报错 改用jawn就好了。在js端jawn更合适
import io.circe.jawn.*
import io.circe.syntax.*
import org.querki.jquery.*
import org.scalajs.dom
import scalatags.JsDom.all.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object TodoListScript:

  @JSExportTopLevel("todoList")
  def todoList(): Unit = $(() =>
    load()
    $("#title").on("keydown", (element: dom.Element, event: JQueryEventObject) => {
      if event.asInstanceOf[dom.KeyboardEvent].keyCode == 13 then
        Option($(element).value()).map(_.asInstanceOf[String]).filter(_.nonEmpty)
          .fold[Unit](dom.window.alert("请输入您要的操作")) { value =>
            saveData(TodoItem(value, false) +: getData())
            load()
            $(element).value("")
          }
    })
    $("ol, ul").on("click", "a", null, (element: dom.Element) => {
      val data = getData()
      val index = $(element).attr("id").asInstanceOf[Int]
      val split = data.splitAt(index + 1)
      saveData(split._1.init ++ split._2)
      load()
    })
    $("ol, ul")on("click", "input", null, (element: dom.Element) => {
      val data = getData()
      val index = $(element).siblings("a").attr("id").asInstanceOf[Int]
      saveData(data.updated(index, data(index).copy(done = $(element).prop("checked").asInstanceOf[Boolean])))
      load()
    })
  )

  def getData(): Vector[TodoItem] =
    Option(dom.window.localStorage.getItem("todolist"))
      .filter(_.nonEmpty)
      .flatMap(decode[Vector[TodoItem]](_).toOption)
      .getOrElse(Vector.empty[TodoItem])

  def saveData(data: Vector[TodoItem]): Unit =
    dom.window.localStorage.setItem("todolist", data.asJson.noSpaces)

  def load(): Unit =
    val data = getData()
    val group = data.zipWithIndex.groupBy(_._1.done)
    val doneList = group.get(true).map(_.map {
      case (TodoItem(title, _), index) => li(
        input(`type` := "checkbox", checked := true),
        p(title),
        a(href := "javascript:;", id := index)
      )
    })
    val todoList = group.get(false).map(_.map {
      case (TodoItem(title, _), index) => li(
        input(`type` := "checkbox"),
        p(title),
        a(href := "javascript:;", id := index)
      )
    })
    $("ul").empty().prepend(doneList.map(frag(_)).getOrElse[Frag](frag()).render.asInstanceOf[dom.Element])
    $("ol").empty().prepend(todoList.map(frag(_)).getOrElse[Frag](frag()).render.asInstanceOf[dom.Element])
    $("#done-count").text(s"${doneList.map(_.size).getOrElse(0)}")
    $("#todo-count").text(s"${todoList.map(_.size).getOrElse(0)}")
