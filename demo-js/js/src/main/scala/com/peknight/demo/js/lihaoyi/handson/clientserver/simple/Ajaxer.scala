package com.peknight.demo.js.lihaoyi.handson.clientserver.simple
import org.scalajs.dom

import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.queue

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
  override def doCall(req: Ajaxer.Request): Future[String] = {
    dom.ext.Ajax.post(
      url = "/ajax/" + req.path.mkString("/"),
      data = upickle.default.write(req.args)
    ).map(_.responseText)
  }

  def read[Result: upickle.default.Reader](p: String) = upickle.default.read[Result](p)

  def write[Result: upickle.default.Writer](r: Result) = upickle.default.write(r)
}
