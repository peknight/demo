package com.peknight.demo.http4s.authentication

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.server.*

object AuthenticationApp:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  case class User(id: Long, name: String)

  // 从请求中获取登录用户信息，IO(???)表示获取用户信息的过程（比如读数据库之类的）
  val authUser: Kleisli[[T] =>> OptionT[IO, T], Request[IO], User] = Kleisli(_ => OptionT.liftF(IO(???)))

  // 创建中间件
  val middleware: AuthMiddleware[IO, User] = AuthMiddleware(authUser)

  // 实际业务逻辑中可以直接获取User
  val authedRoutes: AuthedRoutes[User, IO] = AuthedRoutes.of {
    case GET -> Root / "welcome" as user => Ok(s"Welcome, ${user.name}")
  }

  // 用中间件合成最终服务
  val service: HttpRoutes[IO] = middleware(authedRoutes)

  val spanishRoutes: AuthedRoutes[User, IO] = AuthedRoutes.of {
    case GET -> Root / "hola" as user => Ok(s"Hola, ${user.name}")
  }

  val frenchRoutes: HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "bonjour" => Ok(s"Bonjour")
  }

  val serviceSpanish: HttpRoutes[IO] = middleware(spanishRoutes) <+> frenchRoutes




