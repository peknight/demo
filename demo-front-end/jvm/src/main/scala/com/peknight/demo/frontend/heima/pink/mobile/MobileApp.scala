package com.peknight.demo.frontend.heima.pink.mobile

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.mobile.ctrip.{CtripPage, CtripStyles}
import com.peknight.demo.frontend.heima.pink.mobile.flexlayout.FlexLayoutPage
import com.peknight.demo.frontend.heima.pink.mobile.flowlayout.{FlowLayoutPage, Image2XPage, SpecialPage, ViewportPage}
import com.peknight.demo.frontend.heima.pink.mobile.heimamm.{HeimammMediaStyles, HeimammPage, HeimammStyles}
import com.peknight.demo.frontend.heima.pink.mobile.jd.{JingdongPage, JingdongStyles}
import com.peknight.demo.frontend.heima.pink.mobile.less.{LessPage, LessStyles}
import com.peknight.demo.frontend.heima.pink.mobile.media.{Demo3W320Styles, Demo3W640Styles, MediaQueryPage}
import com.peknight.demo.frontend.heima.pink.mobile.rem.RemPage
import com.peknight.demo.frontend.heima.pink.mobile.suning.{SuningPage, SuningStyles}
import com.peknight.demo.frontend.style.{CommonMediaStyles, NormalizeStyles}
import org.http4s.*
import org.http4s.Charset.`UTF-8`
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.`Content-Type`
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration.*

object MobileApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "viewport" => Ok(ViewportPage.Text.viewportPage)
    case GET -> Root / "image2x" => Ok(Image2XPage.Text.image2xPage)
    case GET -> Root / "special" => Ok(SpecialPage.Text.specialPage)
    case GET -> Root / "flow" => Ok(FlowLayoutPage.Text.flowLayoutPage)
    case GET -> Root / "jingdong" => Ok(JingdongPage.Text.index)
    case GET -> Root / "flex" => Ok(FlexLayoutPage.Text.flexLayoutPage)
    case GET -> Root / "ctrip" => Ok(CtripPage.Text.index)
    case GET -> Root / "media1" => Ok(MediaQueryPage.Text.demo1)
    case GET -> Root / "media2" => Ok(MediaQueryPage.Text.demo2)
    case GET -> Root / "media3" => Ok(MediaQueryPage.Text.demo3)
    case GET -> Root / "less" => Ok(LessPage.Text.less)
    case GET -> Root / "nest" => Ok(LessPage.Text.nest)
    case GET -> Root / "count" => Ok(LessPage.Text.count)
    case GET -> Root / "rem" => Ok(RemPage.Text.rem)
    case GET -> Root / "suning" => Ok(SuningPage.Text.index)
    case GET -> Root / "suning-flexible" => Ok(SuningPage.Text.flexible)
    case GET -> Root / "heimamm" => Ok(HeimammPage.Text.index)
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/mobile").toRoutes

  private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
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
    // case GET -> Root / "swiper.min.css" => fetchCss(uri"https://unpkg.com/swiper@8.4.4/swiper-bundle.min.css")
  }

  private[this] def fetchCss(uri: Uri): IO[Response[IO]] = fetch(uri)(renderCss)

  private[this] def renderCss(styleSheet: StyleSheet.Base): IO[Response[IO]] =
    renderCss(styleSheet.render[String])

  private[this] def renderCss(styleSheet: String): IO[Response[IO]] =
    Ok(styleSheet).map(_.withContentType(`Content-Type`(MediaType.text.css, `UTF-8`)))

  // private[this] val jsRoutes: HttpRoutes[IO] =
  //   HttpRoutes.of[IO] {
  //     case GET -> Root / "flexible.js" =>
  //       fetchJavaScript(uri"https://raw.githubusercontent.com/amfe/lib-flexible/2.0/index.min.js")
  //     case GET -> Root / "swiper.min.js" => fetchJavaScript(uri"https://unpkg.com/swiper@8.4.4/swiper-bundle.min.js")
  //     https://unpkg.com/swiper@8.4.4/swiper-bundle.min.js.map
  //   }

  private[this] def fetchJavaScript(uri: Uri): IO[Response[IO]] = fetch(uri)(renderJavaScript)

  private[this] def renderJavaScript(js: String): IO[Response[IO]] =
    Ok(js).map(_.withContentType(`Content-Type`(MediaType.text.javascript, `UTF-8`)))

  private[this] def fetch(uri: Uri)(f: String => IO[Response[IO]]): IO[Response[IO]] =
    EmberClientBuilder.default[IO].build.use(_.run(GET(uri)).use(_.as[String].flatMap(f)))

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes
      // "js" -> jsRoutes
    )

