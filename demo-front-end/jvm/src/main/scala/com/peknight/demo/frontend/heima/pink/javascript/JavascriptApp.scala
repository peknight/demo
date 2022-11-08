package com.peknight.demo.frontend.heima.pink.javascript

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.javascript.jquery.*
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
    case GET -> Root / "jdpwd" => renderHtml(JingdongPasswordPage.Text.index)
    case GET -> Root / "close-qr" => renderHtml(CloseTaobaoQRCodePage.Text.index)
    case GET -> Root / "sprite-loop" => renderHtml(SpriteLoopPage.Text.index)
    case GET -> Root / "sina-register" => renderHtml(SinaRegisterPage.Text.index)
    case GET -> Root / "baidu-skin" => renderHtml(BaiduChangeSkinPage.Text.index)
    case GET -> Root / "select-all" => renderHtml(SelectAllPage.Text.index)
    case GET -> Root / "switch-tab" => renderHtml(SwitchTablePage.Text.index)
    case GET -> Root / "data-set" => renderHtml(DataSetPage.Text.index)
    case GET -> Root / "sina-dropdown" => renderHtml(SinaDropdownPage.Text.index)
    case GET -> Root / "post-comment" => renderHtml(PostCommentPage.Text.index)
    case GET -> Root / "dynamic-tab" => renderHtml(DynamicTablePage.Text.index)
    case GET -> Root / "angel" => renderHtml(AngelFollowMousePage.Text.index)
    case GET -> Root / "keyboard-event" => renderHtml(KeyboardEventPage.Text.index)
    case GET -> Root / "jd-focus" => renderHtml(JingdongKeyUpFocusPage.Text.index)
    case GET -> Root / "jd-track" => renderHtml(JingdongTrackingNumPage.Text.index)
    case GET -> Root / "count-down" => renderHtml(CountDownPage.Text.index)
    case GET -> Root / "send-msg" => renderHtml(SendMessagePage.Text.index)
    case GET -> Root / "offset" => renderHtml(OffsetPage.Text.index)
    case GET -> Root / "drag-box" => renderHtml(DragBoxPage.Text.index)
    case GET -> Root / "taobao-sidebar" => renderHtml(TaobaoFixedSidebarPage.Text.index)
    case GET -> Root / "import-animate" => renderHtml(ImportAnimatePage.Text.index)
    case GET -> Root / "somersault-cloud" => renderHtml(SomersaultCloudPage.Text.index)
    case GET -> Root / "touch-event" => renderHtml(TouchEventPage.Text.index)
    case GET -> Root / "drag-element" => renderHtml(DragElementPage.Text.index)
    case GET -> Root / "fast-click" => renderHtml(FastClickPage.Text.index)
    case GET -> Root / "video-player" => renderHtml(VideoPlayerPage.Text.index)
    case GET -> Root / "jquery-basic" => renderHtml(JQueryBasicDemoPage.Text.index)
    case GET -> Root / "jquery-dom-convert" => renderHtml(JQueryDomConvertPage.Text.index)
    case GET -> Root / "taobao-apparel" => renderHtml(TaobaoHighQualityApparelPage.Text.index)
    case GET -> Root / "jquery-transition" => renderHtml(JQueryTransitionPage.Text.index)
    case GET -> Root / "high-light-show" => renderHtml(HighLightShowPage.Text.index)
    case GET -> Root / "accordion" => renderHtml(AccordionPage.Text.index)
    case GET -> Root / "scroll-top" => renderHtml(ScrollTopPage.Text.index)
    case GET -> Root / "weibo-publish" => renderHtml(WeiboPublishPage.Text.index)
    case GET -> Root / "masonry" => renderHtml(MasonryLayoutPage.Text.index)
    case GET -> Root / "full-page" => renderHtml(FullPageScrollPage.Text.index)
    case GET -> Root / "bootstrap" => renderHtml(BootstrapPage.Text.index)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.2.1/demo-front-end-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    Router(
      "/" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/javascript").toRoutes,
      "zymedia" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/ireaderlab/zymedia").toRoutes,
      "masonry" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/jquery/pinterestgrid").toRoutes,
      "fullpage" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/jquery/fullpage").toRoutes,
      "webjars" -> webjarServiceBuilder[IO].toRoutes
    )

  def routes(using Logger[IO]): HttpRoutes[IO] = htmlRoutes <+> resourceRoutes

