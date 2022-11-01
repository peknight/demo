package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.VideoPlayerPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class VideoPlayerPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def links: Seq[String] = Seq("/zymedia/zy.media.min.css")
  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override protected def scripts: Seq[String] = Seq("/zymedia/zy.media.min.js")
  override def headTitle: String = "视频播放器"
  override def javaScriptMethod: Option[String] = Some("videoPlayer")
  override def bodyFrag = div(cls := "play-video")(
    // zy_media类名不能改
    div(cls := "zy_media")(
      video(attr("data-config") := "{\"mediaTitle\":\"小蝴蝶\"}")(
        source(src := "/webapis/videos/mov.mp4", `type` := "video/mp4"),
        "您的浏览器不支持HTML5视频"
      ),
    ),
  )

end VideoPlayerPage
object VideoPlayerPage:
  object Text extends VideoPlayerPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".play-video" - (
      zIndex(10),
      position.relative,
      width(300.px),
      height(200.px)
    )
    ".zy_media" - zIndex(100)
  end Styles
end VideoPlayerPage
