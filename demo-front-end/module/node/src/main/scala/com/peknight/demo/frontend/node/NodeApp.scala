package com.peknight.demo.frontend.node

import com.peknight.demo.frontend.node.fs.FileSystem
import org.scalajs.dom

import scala.scalajs.js

object NodeApp extends App:
  val fs = FileSystem
  fs.readFile("./build.sbt", "utf8", (err, data) => {
    Option(err).flatMap(_.toOption).foreach(e => dom.console.log(s"读取文件失败！ ${e.message}"))
    data.toOption.foreach(d => dom.console.log(s"读取文件成功！$d"))
  })
