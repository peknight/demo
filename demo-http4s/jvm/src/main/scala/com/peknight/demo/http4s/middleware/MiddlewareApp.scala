package com.peknight.demo.http4s.middleware

import cats.data.Kleisli
import cats.effect.*
import cats.syntax.all.*
// import com.codahale.metrics.SharedMetricRegistries
import com.peknight.demo.http4s.runLogEmberServer
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
// import org.http4s.metrics.dropwizard.Dropwizard
import org.http4s.metrics.prometheus.{Prometheus, PrometheusExportService}
import org.http4s.server.middleware.{Metrics, RequestId}
import org.http4s.server.{Middleware, Router}
import org.typelevel.ci.*

/**
 * 中间件是对服务(service)的一个包装，它提供了对传入服务的请求(Request)和服务返回的响应(Response)做操作的能力，甚至可以阻止服务被调用
 * 中间件通常是一个简单的接收一个服务作为入参并返回另一个服务的函数
 */
object MiddlewareApp extends IOApp.Simple:

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

  val goodRequest = Request[IO](Method.GET, uri"/")
  val badRequest = Request[IO](Method.GET, uri"/bad")

  val wrappedService = myMiddle(service, "SomeKey" -> "SomeValue")

  // 如果想要中间件在多处复用，也可以将中间件定义为object使用apply方法
  object MyMiddle:
    def addHeader(resp: Response[IO], header: Header.ToRaw) = resp match
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    def apply(service: HttpRoutes[IO], header: Header.ToRaw) = service.map(addHeader(_, header))

  val newService = MyMiddle(service, "SomeKey" -> "SomeValue")

  // 中间件可以支持入参服务的请求、响应 出参服务的请求、响应 分别使用不同的类型
  type AuthMiddleware[F[_], T] = Middleware[F, AuthedRequest[F, T], Response[F], Request[F], Response[F]]

  val apiService = HttpRoutes.of[IO] {
    case GET -> Root / "api" => Ok()
  }

  val aggregateService = apiService <+> MyMiddle(service, "SomeKey" -> "SomeValue")
  val apiRequest = Request[IO](Method.GET, uri"/api")

  // val registry = SharedMetricRegistries.getOrCreate("default")

  // val meteredRoutes = Metrics[IO](Dropwizard(registry, "server"))(apiService)

  val prometheusMeteredRouter: Resource[IO, HttpRoutes[IO]] =
    for
      metricsSvc <- PrometheusExportService.build[IO]
      metrics <- Prometheus.metricsOps[IO](metricsSvc.collectorRegistry, "server")
      router = Router[IO](
        "/api" -> Metrics[IO](metrics)(apiService),
        "/" -> metricsSvc.routes
      )
    yield router

  val requestIdService = RequestId.httpRoutes(HttpRoutes.of[IO] {
    case req =>
      val reqId = req.headers.get(ci"X-Request-ID").fold("null")(_.head.value)
      // use request id to correlate logs with the request
      IO(println(s"request received, cid=$reqId")) *> Ok()
  })

  val responseIO = requestIdService.orNotFound(goodRequest)

  val run =
    for
      goodResp <- service.orNotFound(goodRequest)
      _ <- IO.println(goodResp)
      badResp <- service.orNotFound(badRequest)
      _ <- IO.println(badResp)
      wrappedGoodResp <- wrappedService.orNotFound(goodRequest)
      _ <- IO.println(wrappedGoodResp)
      wrappedBadResp <- wrappedService.orNotFound(badRequest)
      _ <- IO.println(wrappedBadResp)
      newGoodResp <- newService.orNotFound(goodRequest)
      _ <- IO.println(newGoodResp)
      newBadResp <- newService.orNotFound(badRequest)
      _ <- IO.println(newBadResp)
      aggregateGoodResp <- aggregateService.orNotFound(goodRequest)
      _ <- IO.println(aggregateGoodResp)
      aggregateApiResp <- aggregateService.orNotFound(apiRequest)
      _ <- IO.println(aggregateApiResp)
      requestIdResp <- responseIO
      _ <- IO.println(requestIdResp.headers)
      _ <- IO.println(requestIdResp.attributes.lookup(RequestId.requestIdAttrKey))
      // _ <- runLogEmberServer[IO]((aggregateService <+> meteredRoutes).orNotFound)
      _ <- runLogEmberServer[IO]((aggregateService).orNotFound)
    yield ()

