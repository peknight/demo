package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.util.Properties

/**
 * demoJsJVM/reStart 重启调试程序
 * IDEA运行时需要将编译生成的main.js文件放到jvm/target/classes下
 */
object Server {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
//    implicit val materializer = ActorMaterializer()
    val port = Properties.envOrElse("PORT", "8080").toInt
    val route = {
      get {
        pathSingleSlash {
          complete {
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              Page.skeleton.render
            )
          }
        } ~ getFromResourceDirectory("")
      } ~ post {
        path("ajax" / "list") {
          entity(as[String]) { e =>
            complete {
              implicit val writer: upickle.default.Writer[FileData] = upickle.default.macroW
              // Define as many implicit val as types you hav
              upickle.default.write(list(e))
            }
          }
        }
      }
    }
//    Http().bindAndHandle(route, "0.0.0.0", port = port)
    Http().newServerAt("0.0.0.0", port).bindFlow(route)
  }
  def list(path: String) = {
    val (dir, last) = path.splitAt(path.lastIndexOf("/") + 1)
    val files = Option(new java.io.File("./" + dir).listFiles()).toSeq.flatten
    for {
      f <- files if f.getName.startsWith(last)
    } yield FileData(f.getName, f.length())
  }
}