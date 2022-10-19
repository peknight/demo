package com.peknight.demo.frontend.heima.pink.mobile

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.mobile.ctrip.{CtripPage, CtripStyles}
import com.peknight.demo.frontend.heima.pink.mobile.flexlayout.FlexLayoutPage
import com.peknight.demo.frontend.heima.pink.mobile.flowlayout.{FlowLayoutPage, Image2XPage, SpecialPage, ViewportPage}
import com.peknight.demo.frontend.heima.pink.mobile.jd.{JingdongPage, JingdongStyles}
import com.peknight.demo.frontend.heima.pink.mobile.less.{LessPage, LessStyles}
import com.peknight.demo.frontend.heima.pink.mobile.media.{Demo3W320Styles, Demo3W640Styles, MediaQueryPage}
import com.peknight.demo.frontend.heima.pink.mobile.rem.RemPage
import com.peknight.demo.frontend.heima.pink.mobile.suning.{SuningPage, SuningStyles}
import com.peknight.demo.frontend.style.{CommonMediaStyles, NormalizeStyles}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.typelevel.log4cats.Logger
import scalacss.ProdDefaults.*

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
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/mobile").toRoutes

  private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "normalize.css" => Ok(NormalizeStyles.render[String])
    case GET -> Root / "jingdong.css" => Ok(JingdongStyles.render[String])
    case GET -> Root / "ctrip.css" => Ok(CtripStyles.render[String])
    case GET -> Root / "media3w320.css" => Ok(Demo3W320Styles.render[String])
    case GET -> Root / "media3w640.css" => Ok(Demo3W640Styles.render[String])
    case GET -> Root / "less.css" => Ok(LessStyles.render[String])
    case GET -> Root / "common.css" => Ok(CommonMediaStyles.render[String])
    case GET -> Root / "suning.css" => Ok(SuningStyles.render[String])
  }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes
    )
