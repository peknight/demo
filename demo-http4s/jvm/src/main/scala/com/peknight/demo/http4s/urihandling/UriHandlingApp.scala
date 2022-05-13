package com.peknight.demo.http4s.urihandling

import cats.effect.*
import org.http4s.*
import org.http4s.UriTemplate.*
import org.http4s.implicits.*

object UriHandlingApp extends IOApp.Simple:

  given CanEqual[Uri, Uri] = CanEqual.derived

  val uri = uri"http://http4s.org"

  val docs = uri.withPath(path"/docs/0.15")

  val docs2 = uri / "docs" / "0.15"

  val template = UriTemplate(
    authority = Some(Uri.Authority(host = Uri.RegName("http4s.org"))),
    scheme = Some(Uri.Scheme.http),
    path = List(PathElm("docs"), PathElm("0.15"))
  )

  // val configuredUri = Configured[String].flatMap(s => Configured(_ => Uri.fromString(s).toOption))

  def run =
    for
      _ <- IO.println(uri)
      _ <- IO.println(docs == docs2)
      _ <- IO.println(template.toUriIfPossible)
    yield ()
