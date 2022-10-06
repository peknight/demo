package com.peknight.demo.http4s.staticfiles

import cats.effect.*
import com.peknight.demo.http4s.runLogEmberServer
import fs2.io.file.Path
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.server.*
import org.http4s.server.staticcontent.*
import org.http4s.server.staticcontent.WebjarServiceBuilder.WebjarAsset

object StaticFilesApp extends IOApp.Simple:

  given CanEqual[Uri.Path, Uri.Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val fileRouter = fileService[IO](FileService.Config("."))

  val anotherService = HttpRoutes.of[IO] { case _ => Ok() }

  // 运行时目录，只能看html文件或者其它普通文件，不能看目录
  val routes = HttpRoutes.of[IO] { case request @ GET -> Root / "index.html" =>
    StaticFile.fromPath(
      Path("./demo-http4s/jvm/src/main/resources/index.html"),
      Some(request)
    ).getOrElseF(NotFound())
  }

  // 读取jar包内的资源文件 （这里读取resource/inner下的文件）
  val resourceRoutes = resourceServiceBuilder[IO]("/inner").toRoutes

  def static(file: String, request: Request[IO]) =
    StaticFile.fromResource("/" + file, Some(request)).getOrElseF(NotFound())

  val fileTypes = List(".js", ".css", ".map", ".html", ".webm")

  val fileRoutes = HttpRoutes.of[IO] {
    case request @ GET -> Root / path if fileTypes.exists(path.endsWith) => static(path, request)
  }

  def isJsAsset(asset: WebjarAsset): Boolean = asset.asset.endsWith(".js")

  // webjars访问
  val webjars: HttpRoutes[IO] = webjarServiceBuilder[IO].withWebjarAssetFilter(isJsAsset).toRoutes

  val httpApp: HttpApp[IO] = Router(
    "api" -> anotherService,
    "h5" -> fileService[IO](FileService.Config("./demo-http4s/jvm/src/main/resources")),
    "path" -> routes,
    "resource" -> resourceRoutes,
    "file" -> fileRoutes,
    // http://localhost:8080/webjars/jquery/3.6.0/jquery.js
    "webjars" -> webjars
  ).orNotFound

  val run =
    for
      _ <- runLogEmberServer[IO](httpApp)
    yield ()

