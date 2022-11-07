package com.peknight.demo.frontend.heima.pink.pinyougou.app

import cats.effect.*
import cats.syntax.semigroupk.*
import com.peknight.demo.frontend.app.DemoFrontEndHttp4sApp
import com.peknight.demo.frontend.heima.pink.mobile.jd.{JingdongPage, JingdongStyles}
import com.peknight.demo.frontend.heima.pink.pinyougou.page.*
import com.peknight.demo.frontend.heima.pink.pinyougou.style.*
import com.peknight.demo.frontend.style.NormalizeStyles
import fs2.io.file
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.*
import org.typelevel.log4cats.Logger

object PinyougouApp extends DemoFrontEndHttp4sApp:

  private[this] val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => renderHtml(IndexPage.Text.index)
    case GET -> Root / "index.html" => renderHtml(IndexPage.Text.index)
    case GET -> Root / "list.html" => renderHtml(ListPage.Text.list)
    case GET -> Root / "register.html" => renderHtml(RegisterPage.Text.register)
    case GET -> Root / "detail.html" => renderHtml(DetailPage.Text.detail)
    case GET -> Root / "cart.html" => renderHtml(ShoppingCartPage.Text.cart)
    case GET -> Root / "jingdong" => renderHtml(JingdongPage.Text.index)
    case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
      StaticFile.fromPath(file.Path(s"./demo-front-end/js/target/scala-3.2.1/demo-front-end-opt/$path"), Some(req))
        .getOrElseF(NotFound())
  }

  private[this] val resourceRoutes: HttpRoutes[IO] =
    Router(
      "/" -> (resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/pinyougou").toRoutes <+>
        resourceServiceBuilder[IO]("/com/peknight/demo/frontend/heima/pink/mobile").toRoutes),
      "easylazyload" -> resourceServiceBuilder[IO]("/com/peknight/demo/frontend/jquery/easylazyload").toRoutes,
      "webjars" -> webjarServiceBuilder[IO].toRoutes
    )

  private[this] val cssRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "base.css" => renderCss(BaseStyles)
    case GET -> Root / "fonts.css" => renderCss(FontsStyles)
    case GET -> Root / "common.css" => renderCss(CommonStyles)
    case GET -> Root / "index.css" => renderCss(IndexStyles)
    case GET -> Root / "list.css" => renderCss(ListStyles)
    case GET -> Root / "register.css" => renderCss(RegisterStyles)
    case GET -> Root / "detail.css" => renderCss(DetailStyles)
    case GET -> Root / "cart.css" => renderCss(ShoppingCartStyles)
    case GET -> Root / "normalize.css" => renderCss(NormalizeStyles)
    case GET -> Root / "jingdong.css" => renderCss(JingdongStyles)
  }

  def routes(using Logger[IO]): HttpRoutes[IO] =
    Router(
      "/" -> (htmlRoutes <+> resourceRoutes),
      "css" -> cssRoutes
    )