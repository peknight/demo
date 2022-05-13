package com.peknight.demo.http4s.testing

import cats.effect.*
import cats.syntax.functor.*
import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.Client
import org.http4s.dsl.io.*
import org.http4s.implicits.*


object TestingApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[Status, Status] = CanEqual.derived
  given CanEqual[Json, Json] = CanEqual.derived

  given Encoder[User] = deriveEncoder[User]

  def service[F[_]: Async](repo: UserRepo[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "user" / id => repo.find(id).map {
      case Some(user) => Response(status = Status.Ok).withEntity(user.asJson)
      case None => Response(status = Status.NotFound)
    }
  }

  def check[A](actual: IO[Response[IO]], expectedStatus: Status, expectedBody: Option[A])
              (using EntityDecoder[IO, A], CanEqual[A, A]): IO[Boolean] =
    for
      actualResp <- actual
      statusCheck = actualResp.status == expectedStatus
      bodyCheck <- expectedBody.fold[IO[Boolean]](actualResp.body.compile.toVector.map(_.isEmpty))(expected =>
        actualResp.as[A].map(_ == expected)
      )
    yield statusCheck && bodyCheck

  val success: UserRepo[IO] = _ => IO.pure(Some(User("johndoe", 42)))

  def response(userRepo: UserRepo[IO]): IO[Response[IO]] =
    service[IO](userRepo).orNotFound.run(Request(method = Method.GET, uri = uri"/user/not-used"))

  val expectedJson = Json.obj("name" := "johndoe", "age" := 42)

  val foundNone: UserRepo[IO] = _ => IO.pure(None)

  val doesNotMatter: UserRepo[IO] = _ => IO.raiseError(new RuntimeException("Should not get called!"))

  val respNotFound: IO[Response[IO]] = service[IO](doesNotMatter).orNotFound.run(
    Request(method = Method.GET, uri = uri"/not-a-matching-path")
  )

  val httpApp: HttpApp[IO] = service[IO](success).orNotFound

  val request: Request[IO] = Request(method = Method.GET, uri = uri"/user/not-used")

  val client: Client[IO] = Client.fromHttpApp(httpApp)

  val resp: IO[Json] = client.expect[Json](request)

  def run =
    for
      successResponse <- check[Json](response(success), Status.Ok, Some(expectedJson))
      _ <- IO.println(successResponse)
      foundNoneResponse <- check[Json](response(foundNone), Status.NotFound, None)
      _ <- IO.println(foundNoneResponse)
      notMatterResponse <- check[String](respNotFound, Status.NotFound, Some("Not found"))
      _ <- IO.println(notMatterResponse)
      r <- resp
      _ <- IO.println(r == expectedJson)
    yield ()
