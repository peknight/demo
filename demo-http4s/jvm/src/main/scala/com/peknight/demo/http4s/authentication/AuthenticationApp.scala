package com.peknight.demo.http4s.authentication

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.http4s.runLogEmberServer
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.server.*

object AuthenticationApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  case class User(id: Long, name: String)

  // 从请求中获取登录用户信息，IO(???)表示获取用户信息的过程（比如读数据库之类的）
  val authUser: Kleisli[[T] =>> OptionT[IO, T], Request[IO], User] =
    Kleisli(_ => OptionT.liftF(IO(User(123, "user_test"))))

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

  // AuthMiddleware会消费所有请求 请求不匹配路由或是未通过验证都会返回401。这里访问/bonjour就会401
  val serviceSpanish: HttpRoutes[IO] = middleware(spanishRoutes) <+> frenchRoutes

  // 如果想不走验证正常访问/bonjour可以用以下方式

  // 方法1 加固定前缀
  val serviceRouter = Router(
    "/spanish" -> middleware(spanishRoutes),
    "/french" -> frenchRoutes
  )
  // 方法2 用withFallThrough
  val middlewareWithFallThrough: AuthMiddleware[IO, User] = AuthMiddleware.withFallThrough(authUser)
  val serviceSF: HttpRoutes[IO] = middlewareWithFallThrough(spanishRoutes) <+> frenchRoutes
  // 方法3 把AuthedRoutes定义在最后
  val serviceFS: HttpRoutes[IO] = frenchRoutes <+> middlewareWithFallThrough(spanishRoutes)
  // 如果不想在验证失败时总是返回401，可以使用noSpider方法指定onAuthFailure handler
  // AuthMiddleware.noSpider()

  val authUserEither: Kleisli[IO, Request[IO], Either[String, User]] =
    Kleisli(_ => IO(Either.right(User(123, "user_test"))))

  val onFailure: AuthedRoutes[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.context)))

  val authMiddleware = AuthMiddleware(authUserEither, onFailure)

  val serviceKleisli: HttpRoutes[IO] = authMiddleware(authedRoutes)

  val run =
    for
      _ <- runLogEmberServer[IO](serviceFS.orNotFound)
    yield ()

