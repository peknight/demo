package com.peknight.demo.frontend.heima.pink.javascript

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.javascript.webapis.*
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

object JavascriptApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "jdpwd" => Ok(JingdongPasswordPage.Text.index)
    case GET -> Root / "close-qr" => Ok(CloseTaobaoQRCodePage.Text.index)
    case GET -> Root / "sprite-loop" => Ok(SpriteLoopPage.Text.index)
    case GET -> Root / "sina-register" => Ok(SinaRegisterPage.Text.index)
    case GET -> Root / "baidu-skin" => Ok(BaiduChangeSkinPage.Text.index)
    case GET -> Root / "select-all" => Ok(SelectAllPage.Text.index)
    case GET -> Root / "switch-tab" => Ok(SwitchTablePage.Text.index)
    case GET -> Root / "data-set" => Ok(DataSetPage.Text.index)
    case GET -> Root / "sina-dropdown" => Ok(SinaDropdownPage.Text.index)
    case GET -> Root / "post-comment" => Ok(PostCommentPage.Text.index)
    case GET -> Root / "dynamic-tab" => Ok(DynamicTablePage.Text.index)
    case GET -> Root / "angel" => Ok(AngelFollowMousePage.Text.index)
    case GET -> Root / "keyboard-event" => Ok(KeyboardEventPage.Text.index)
    case GET -> Root / "jd-focus" => Ok(JingdongKeyUpFocusPage.Text.index)
    case GET -> Root / "jd-track" => Ok(JingdongTrackingNumPage.Text.index)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.2.0/demo-front-end-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    Router(
      "/" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/javascript").toRoutes,
    )

  def routes(using Logger[IO]): HttpRoutes[IO] = htmlRoutes <+> resourceRoutes

