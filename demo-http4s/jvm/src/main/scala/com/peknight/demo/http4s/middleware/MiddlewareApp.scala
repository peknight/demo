package com.peknight.demo.http4s.middleware

import cats.data.Kleisli
import cats.effect.*
import cats.syntax.all.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*

/**
 * 中间件是对服务(service)的一个包装，它提供了对传入服务的请求(Request)和服务返回的响应(Response)做操作的能力，甚至可以阻止服务被调用
 * 中间件通常是一个简单的接收一个服务作为入参并返回另一个服务的函数
 */
object MiddlewareApp:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def myMiddle(service: HttpRoutes[IO], header: Header.ToRaw): HttpRoutes[IO] = Kleisli { (req: Request[IO]) =>
    service(req).map {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    }
  }

  val service = HttpRoutes.of[IO] {
    case GET -> Root / "bad" => BadRequest()
    case _ => Ok()
  }

