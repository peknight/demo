package com.peknight.demo.natchez

import cats.effect.{IO, IOApp}
import natchez.Span
import org.http4s.Method.GET
import org.http4s.client.Client
import org.http4s.client.dsl.io.*
import org.http4s.{EntityDecoder, Header, Uri}

object KernelsApp extends IOApp.Simple:
  def makeRequest[A](span: Span[IO], client: Client[IO], uri: Uri)(using ev: EntityDecoder[IO, A]): IO[A] =
    span.kernel.flatMap { k =>
      // turn a Map[String, String] into List[Header]
      val http4sHeaders = k.toHeaders.map { case (k, v) => Header.Raw(k, v) }.toSeq
      client.expect[A](GET(uri).withHeaders(http4sHeaders))
    }
  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield
      ()
end KernelsApp
