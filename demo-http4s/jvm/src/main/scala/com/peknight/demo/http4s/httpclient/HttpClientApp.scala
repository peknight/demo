package com.peknight.demo.http4s.httpclient

import cats.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.http4s.logEmberServer
import org.http4s.*
import org.http4s.client.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.*
import org.http4s.implicits.*
import org.typelevel.ci.CIString

import java.util.concurrent.*
import scala.concurrent.duration.*

object HttpClientApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val app = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
  }.orNotFound

  val server = logEmberServer[IO](app)

  val helloTom = EmberClientBuilder.default[IO].build.use { client =>
    client.expect[String]("http://localhost:8080/hello/Tom")
  }

  // 不知道文档里定义这个池子是干啥的 也不用
  val blockingPool = Executors.newFixedThreadPool(5)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create

  val helloJames = httpClient.expect[String]("http://localhost:8080/hello/James")

  def hello(name: String): IO[String] =
    val target = uri"http://localhost:8080/hello/" / name
    httpClient.expect[String](target)

  val people = Vector("Michael", "Jessica", "Ashley", "Christopher")

  val greetingList = people.parTraverse(hello)

  val greetingStringEffect = greetingList.map(_.mkString("\n"))

  val goodUri = uri"https://my-awesome-service.com/foo/bar?wow=yeah"

  // 编译不通过
  // val badUri = uri"bad uri"

  val validUri = "https://my-awesome-service.com/foo/bar?wow=yeah"

  val invalidUri = "yeah whatever"

  val uri: Either[ParseFailure, Uri] = Uri.fromString(validUri)

  val parseFailure: Either[ParseFailure, Uri] = Uri.fromString(invalidUri)

  val baseUri: Uri = uri"http://foo.com"

  val withPath: Uri = baseUri.withPath(path"/bar/baz")

  val withQuery: Uri = withPath.withQueryParam("hello", "world")

  def mid(f: Int => String): Int => String = in =>
    val resultOfF = f(in + 1)
    s"${f(in)} is the original result, but $resultOfF's input was modified!'"

  val f1: Int => String = _.toString

  val f2: Int => String = mid(f1)

  def addTestHeader[F[_]: MonadCancelThrow](underlying: Client[F]): Client[F] = Client[F] { req =>
    underlying.run(req.withHeaders(Header.Raw(CIString("X-Test-Request"), "test")))
      .map(_.withHeaders(Header.Raw(CIString("X-Test-Response"), "test")))
  }

  val run =
    for
      allocatedServer <- server.allocated
      shutdown = allocatedServer._2
      tom <- helloTom
      _ <- IO.println(tom)
      james <- helloJames
      _ <- IO.println(james)
      greetingString <- greetingStringEffect
      _ <- IO.println(greetingString)
      _ <- IO.println(goodUri)
      _ <- IO.println(uri)
      _ <- IO.println(parseFailure)
      _ <- IO.println(baseUri)
      _ <- IO.println(withPath)
      _ <- IO.println(withQuery)
      _ <- IO.println(f1(10))
      _ <- IO.println(f2(10))
    yield ()
