package com.peknight.demo.natchez

import cats.effect.{IO, IOApp}
import natchez.{EntryPoint, Kernel, Trace}
import com.peknight.demo.natchez.SpansApp.wibble
import org.http4s.dsl.io.*
import org.http4s.{HttpRoutes, Method}

object EntryPointsApp extends IOApp.Simple:
  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def routes(ep: EntryPoint[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      ep.root("hello").use { span =>
        span.put("the-name" -> name) *> Ok(s"Hello, $name.")
      }
  }

  def continuedRoutes(ep: EntryPoint[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ GET -> Root / "hello" / name =>
      val k: Kernel = Kernel(req.headers.headers.map { h => h.name -> h.value }.toMap)
      ep.continueOrElseRoot("hello", k).use { span =>
        span.put("the-name" -> name) *> Ok(s"Hello, $name.")
      }
    case req @ GET -> Root / "wibble" =>
      for
        trace <- Trace.ioTraceForEntryPoint(ep)
        given Trace[IO] = trace
        _ <- wibble[IO]("pek", 18)
        res <- Ok(s"Hello, wibble")
      yield
        res
  }

  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield
      ()
end EntryPointsApp
