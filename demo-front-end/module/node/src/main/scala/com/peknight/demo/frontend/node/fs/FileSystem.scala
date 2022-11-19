package com.peknight.demo.frontend.node.fs

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
// 导入模块本身 使用JSImport.Namespace
@JSImport("fs", JSImport.Namespace)
object FileSystem extends js.Object:
  def readFile(path: String | Int, options: js.UndefOr[ReadFileOption | String] = js.undefined,
               callback: js.Function2[js.UndefOr[Error], js.UndefOr[String], Unit]): Unit = js.native
