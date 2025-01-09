package com.peknight.demo.frontend.professional

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.professional.form.richtext.RichTextPage
import fs2.io.file
import org.http4s.*
import org.http4s.Charset.`UTF-8`
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.`Content-Type`
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.{resourceServiceBuilder, webjarServiceBuilder}
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import scalacss.ProdDefaults.*
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration.*

object ProfessionalApp extends DemoFrontEndHttp4sApp:

  private val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "rich-text-iframe" => Ok(RichTextPage.Text.iframeIndex)
    case GET -> Root / "rich-text-blank.htm" => Ok(RichTextPage.Text.blank)
    case GET -> Root / "rich-text-contenteditable" => Ok(RichTextPage.Text.contentEditableIndex)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.6.2/demo-front-end-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  def routes(using Logger[IO]): HttpRoutes[IO] = htmlRoutes

