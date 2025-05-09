package com.peknight.demo.frontend.heima.pink.mobile

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.javascript.webapis.BootstrapCarouselPage
import com.peknight.demo.frontend.heima.pink.mobile.alibaixiu.AlibaixiuPage
import com.peknight.demo.frontend.heima.pink.mobile.bilibili.BilibiliPage
import com.peknight.demo.frontend.heima.pink.mobile.bootstrap.BootstrapPage
import com.peknight.demo.frontend.heima.pink.mobile.ctrip.{CtripPage, CtripStyles}
import com.peknight.demo.frontend.heima.pink.mobile.flexlayout.FlexLayoutPage
import com.peknight.demo.frontend.heima.pink.mobile.flowlayout.{FlowLayoutPage, Image2XPage, SpecialPage, ViewportPage}
import com.peknight.demo.frontend.heima.pink.mobile.heimamm.{HeimammMediaStyles, HeimammPage, HeimammStyles}
import com.peknight.demo.frontend.heima.pink.mobile.jd.{JingdongPage, JingdongStyles}
import com.peknight.demo.frontend.heima.pink.mobile.less.{LessPage, LessStyles}
import com.peknight.demo.frontend.heima.pink.mobile.media.{Demo3W320Styles, Demo3W640Styles, MediaQueryPage}
import com.peknight.demo.frontend.heima.pink.mobile.rem.RemPage
import com.peknight.demo.frontend.heima.pink.mobile.responsive.ResponsivePage
import com.peknight.demo.frontend.heima.pink.mobile.suning.{SuningPage, SuningStyles}
import com.peknight.demo.frontend.heima.pink.mobile.viewportwidth.ViewportWidthPage
import com.peknight.demo.frontend.style.{CommonMediaStyles, NormalizeStyles}
import fs2.io.file
import org.http4s.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.{resourceServiceBuilder, webjarServiceBuilder}
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration.*

object MobileApp extends DemoFrontEndHttp4sApp:

  private val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "viewport" => renderHtml(ViewportPage.Text.viewportPage)
    case GET -> Root / "image2x" => renderHtml(Image2XPage.Text.image2xPage)
    case GET -> Root / "special" => renderHtml(SpecialPage.Text.specialPage)
    case GET -> Root / "flow" => renderHtml(FlowLayoutPage.Text.flowLayoutPage)
    case GET -> Root / "jingdong" => renderHtml(JingdongPage.Text.index)
    case GET -> Root / "flex" => renderHtml(FlexLayoutPage.Text.flexLayoutPage)
    case GET -> Root / "ctrip" => renderHtml(CtripPage.Text.index)
    case GET -> Root / "media1" => renderHtml(MediaQueryPage.Text.demo1)
    case GET -> Root / "media2" => renderHtml(MediaQueryPage.Text.demo2)
    case GET -> Root / "media3" => renderHtml(MediaQueryPage.Text.demo3)
    case GET -> Root / "less" => renderHtml(LessPage.Text.less)
    case GET -> Root / "nest" => renderHtml(LessPage.Text.nest)
    case GET -> Root / "count" => renderHtml(LessPage.Text.count)
    case GET -> Root / "rem" => renderHtml(RemPage.Text.rem)
    case GET -> Root / "suning" => renderHtml(SuningPage.Text.index)
    case GET -> Root / "suning-flexible" => renderHtml(SuningPage.Text.flexible)
    case GET -> Root / "heimamm" => renderHtml(HeimammPage.Text.index)
    case GET -> Root / "responsive" => renderHtml(ResponsivePage.Text.index)
    case GET -> Root / "bootstrap" => renderHtml(BootstrapPage.Text.index)
    case GET -> Root / "alibaixiu" => renderHtml(AlibaixiuPage.Text.index)
    case GET -> Root / "vw" => renderHtml(ViewportWidthPage.Text.index)
    case GET -> Root / "bilibili" => renderHtml(BilibiliPage.Text.index)
    case GET -> Root / "bootstrap-carousel" => renderHtml(BootstrapCarouselPage.Text.index)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.7.0/demo-front-end-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  private val resourceRoutes: HttpRoutes[IO] =
    Router(
      "/" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/mobile").toRoutes,
      "webjars" -> webjarServiceBuilder[IO].toRoutes
    )

  private val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "normalize.css" => renderCss(NormalizeStyles)
    case GET -> Root / "jingdong.css" => renderCss(JingdongStyles)
    case GET -> Root / "ctrip.css" => renderCss(CtripStyles)
    case GET -> Root / "media3w320.css" => renderCss(Demo3W320Styles)
    case GET -> Root / "media3w640.css" => renderCss(Demo3W640Styles)
    case GET -> Root / "less.css" => renderCss(LessStyles)
    case GET -> Root / "common.css" => renderCss(CommonMediaStyles)
    case GET -> Root / "suning.css" => renderCss(SuningStyles)
    case GET -> Root / "heimamm.css" => renderCss(HeimammStyles)
    case GET -> Root / "heimamm-media.css" => renderCss(HeimammMediaStyles)
  }

  private val jsRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ GET -> Root / "heimamm" / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/module/heimamm/target/scala-3.7.0/demo-front-end-module-heimamm-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes,
      "js" -> jsRoutes,
    )

