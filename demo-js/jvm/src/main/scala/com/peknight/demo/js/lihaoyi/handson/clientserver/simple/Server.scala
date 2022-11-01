package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import cats.effect.*
import com.comcast.ip4s.*
import fs2.io.file
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.scalatags.*
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent.*

object Server extends Api[IO] with IOApp.Simple:
  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def list(path: FilePath): IO[Seq[FileData]] =
    val (dir, last) = path.path.splitAt(path.path.lastIndexOf("/") + 1)
    file.Files[IO].walk(file.Path(dir), 1, false)
      .filter(_.fileName.toString.startsWith(last))
      .evalMap(p => file.Files[IO].size(p).map(size => FileData(p.fileName.toString, size)))
      .compile.toList

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(Page.skeleton)
    case req @ GET -> Root / "main.js" =>
      StaticFile.fromPath(file.Path("./demo-js/js/target/scala-3.2.1/demo-js-opt/main.js"), Some(req))
        .getOrElseF(NotFound())
    case req @ POST -> "ajax" /: rest =>
      given EntityDecoder[IO, FilePath] = jsonOf[IO, FilePath]
      for
        filePath <- req.as[FilePath]
        fileDataSeq <- list(filePath)
        resp <- Ok(fileDataSeq.asJson)
      yield resp
  }

  val server = EmberServerBuilder.default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(Logger.httpApp(true, true)(routes.orNotFound))
    .build
    .use(_ => IO.never)

  def run = server

