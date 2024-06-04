package com.peknight.demo.frontend.vue

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.javascript.datavisualization.*
import com.peknight.demo.frontend.heima.pink.javascript.echarts.*
import com.peknight.demo.frontend.heima.pink.javascript.jquery.*
import com.peknight.demo.frontend.heima.pink.javascript.webapis.*
import com.peknight.demo.frontend.vue.introduction.{CompositionApiPage, IntroductionPage, OptionsApiPage}
import com.peknight.demo.frontend.vue.quickstart.UseVuePage
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

object VueApp extends DemoFrontEndHttp4sApp:

  private val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "introduction" => renderHtml(IntroductionPage.Text.esm)
    case GET -> Root / "options-api" => renderHtml(OptionsApiPage.Text.esm)
    case GET -> Root / "composition-api" => renderHtml(CompositionApiPage.Text.esm)
    case GET -> Root / "use-vue" => renderHtml(UseVuePage.Text.index)
    case GET -> Root / "use-vue-esm" => renderHtml(UseVuePage.Text.esm)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.4.2/demo-front-end-opt/$path"), Some(req))
        .orElse(StaticFile.fromPath(file.Path(s"./demo-front-end/module/demo-vue/target/scala-3.4.2/demo-front-end-module-demo-vue-opt/$path"), Some(req)))
        .getOrElseF(NotFound())
  }

  private val resourceRoutes: HttpRoutes[IO] =
    Router(
      "webjars" -> webjarServiceBuilder[IO].toRoutes
    )

  def routes(using Logger[IO]): HttpRoutes[IO] = Router(
    "/" -> (htmlRoutes <+> resourceRoutes),
  )

