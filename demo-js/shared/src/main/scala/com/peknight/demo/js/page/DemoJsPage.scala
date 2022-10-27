package com.peknight.demo.js.page

import scalatags.generic.Bundle
import scalatags.text.Builder

class DemoJsPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{style as inlineStyle, title as _, *}
  import bundle.tags2.{nav, style, title}

  private[this] def skeleton(headTitle: String)(bodyFrag: Modifier*): Frag =
    html(
      head(
        meta(charset := "UTF-8"),
        title(headTitle)
      ),
      body(
        script(`type` := "text/javascript", src := "/main.js"),
        bodyFrag
      )
    )

  private[this] def domSkeleton(headTitle: String)(bodyFrag: Modifier*): Frag =
    skeleton(s"Scala.JS Demo: $headTitle")(bodyFrag)

  private[this] def canvasSkeleton(headTitle: String, func: String, width: Int, height: Int): Frag =
    skeleton(s"Hands on Scala.JS Canvas Demo: $headTitle")(
      div(canvas(inlineStyle := s"width: ${width}px; height: ${height}px;", id := "hands_on_canvas")),
      script(s"${func}(document.getElementById('hands_on_canvas'))")
    )

  private[this] def webpageSkeleton(headTitle: String, func: String): Frag =
    skeleton(s"Hands on Scala.JS Webpage Demo: $headTitle")(
      div(id := "hands_on_webpage"),
      script(s"${func}(document.getElementById('hands_on_webpage'))")
    )

  val index: Frag = skeleton("Scala.JS Demo Home")(
    h2("Scala.JS Tutorial"),
    p(a(href := "/tutorial")("Tutorial")),
    h2("Scala.JS dom"),
    p(a(href := "/alert")("Alert Demo")),
    p(a(href := "/node-append-child")("Node Append Child Demo")),
    p(a(href := "/node-on-mouse-move")("Node On Mouse Move Demo")),
    p(a(href := "/dom-btoa")("BTOA Demo")),
    p(a(href := "/dom-local-storage")("Local Storage Demo")),
    p(a(href := "/dom-html-canvas-element")("HTML Canvas Element Demo")),
    p(a(href := "/dom-fetch")("Dom Fetch Demo")),
    p(a(href := "/dom-websocket")("Websocket Demo")),
    p(a(href := "/element-style")("Element Style Demo")),
    h2("Lihaoyi Canvas"),
    p(a(href := "/hands-on-canvas")("Canvas Tutorial Demo")),
    p(a(href := "/sierpinski-triangle")("Sierpinski Triangle Demo")),
    p(a(href := "/space-invaders")("Space Invaders Demo")),
    p(a(href := "/dodge-the-dots")("Dodge The Dots Demo")),
    p(a(href := "/sketchpad")("Sketchpad Demo")),
    p(a(href := "/canvas-clock")("Canvas Clock Demo")),
    p(a(href := "/flappy-box")("Flappy Box Demo")),
    p(a(href := "/canvas-async-advanced")("Async Advanced Demo")),
    h2("Lihaoyi Webpage"),
    p(a(href := "/hello-world-0")("Hello World 0 Demo")),
    p(a(href := "/hello-world-1")("Hello World 1 Demo")),
    p(a(href := "/capital-box")("Capital Box Demo")),
    p(a(href := "/re-rendering")("Re Rendering Demo")),
    p(a(href := "/weather-1")("Weather 1 Demo")),
    p(a(href := "/weather-search")("Weather Search Demo")),
    p(a(href := "/future-weather")("Future Weather Demo"))
  )

  val tutorial: Frag = domSkeleton("The Scala.js Tutorial")(
    button(id := "click-me-button", `type` := "button", onclick := "addClickedMessage()")("Click me!"),
    script("tutorial()")
  )

  val alertDemo: Frag = domSkeleton("Alert Demo")(
    button(id := "alert-demo-button", onclick := "alertDemo()")("Alert!")
  )

  val nodeAppendChildDemo: Frag = domSkeleton("Node Append Child Demo")(
    button(
      id := "node-append-child-demo-button",
      `type` := "button",
      onclick := "nodeAppendChildDemo(document.getElementById('node-append-child-demo'))"
    )("Node.appendChild"),
    div(id := "node-append-child-demo")("div")
  )

  val nodeOnMouseMoveDemo: Frag = domSkeleton("Node On Mouse Move")(
    pre(id := "node-on-mouse-move-demo")("Hover this box!"),
    script("nodeOnMouseMoveDemo(document.getElementById('node-on-mouse-move-demo'))")
  )

  val domBtoaDemo: Frag = domSkeleton("BTOA")(
    input(id := "dom-btoa-demo-in", placeholder := "Enter text to b64 encode"),
    div(id := "dom-btoa-demo-out"),
    script("domBtoaDemo(document.getElementById('dom-btoa-demo-in'), document.getElementById('dom-btoa-demo-out'))")
  )

  val domLocalStorageDemo: Frag = domSkeleton("Local Storage")(
    input(id := "dom-local-storage-demo-in"),
    div(id := "dom-local-storage-demo-out"),
    script("domLocalStorageDemo(document.getElementById('dom-local-storage-demo-in'), " +
      "document.getElementById('dom-local-storage-demo-out'))")
  )

  val domHtmlCanvasElementDemo: Frag = domSkeleton("HTML Canvas Element")(
    div(canvas(id := "dom-html-canvas-element-demo")),
    script("domHTMLCanvasElementDemo(document.getElementById('dom-html-canvas-element-demo'))")
  )

  val domFetchDemo: Frag = domSkeleton("Dom Fetch")(
    button(
      id := "dom-fetch-demo-button",
      `type` := "button",
      onclick := "domFetchDemo(document.getElementById('dom-fetch-demo'))"
    )("dom.Fetch"),
    div(id := "dom-fetch-demo")("output")
  )

  val domWebsocketDemo: Frag = domSkeleton("Websocket")(
    input(id := "dom-websocket-demo-in", placeholder := "Type something in"),
    pre(id := "dom-websocket-demo-out")("output"),
    script("domWebsocketDemo(document.getElementById('dom-websocket-demo-in'), document.getElementById('dom-websocket-demo-out'))")
  )

  val elementStyleDemo: Frag = domSkeleton("Element Style")(
    button(
      id := "element-style-demo-button",
      `type` := "button",
      onclick := "elementStyleDemo(document.getElementById('element-style-demo'))"
    )("Element.style"),
    div(id := "element-style-demo")("div")
  )

  val handsOnCanvasTutorialDemo: Frag = canvasSkeleton("Tutorial", "handsOn", 255, 255)

  val sierpinskiTriangleDemo: Frag =
    canvasSkeleton("SierpinskiTriangle", "sierpinskiTriangle", 255, 255)

  val spaceInvadersDemo: Frag = canvasSkeleton("Space Invaders", "spaceInvaders", 255, 255)

  val dodgeTheDotsDemo: Frag = canvasSkeleton("Dodge The Dots", "dodgeTheDots", 255, 255)

  val sketchpadDemo: Frag = canvasSkeleton("Sketchpad", "sketchpad", 255, 255)

  val canvasClockDemo: Frag = canvasSkeleton("Canvas Clock", "canvasClock", 511, 255)

  val flappyBoxDemo: Frag = canvasSkeleton("Flappy Box", "flappyBox", 800, 400)

  val asyncAdvancedDemo: Frag = skeleton(s"Hands on Scala.JS Canvas Demo: Async Advanced")(
    div(inlineStyle := "float: left;")(canvas(id := "hands_on_canvas")),
    script(s"AsyncAdvanced.main(document.getElementById('hands_on_canvas'))")
  )

  val helloWorld0Demo: Frag = webpageSkeleton("Hello World 0", "helloWorld0")
  val helloWorld1Demo: Frag = webpageSkeleton("Hello World 1", "helloWorld1")
  val capitalBoxDemo: Frag = webpageSkeleton("Capital Box", "capitalBox")
  val reRenderingDemo: Frag = webpageSkeleton("Re Rendering", "reRendering")
  val weather1Demo: Frag = webpageSkeleton("Weather 1", "weather1")
  val weatherSearchDemo: Frag = webpageSkeleton("Weather Search", "weatherSearch")
  val futureWeatherDemo: Frag = webpageSkeleton("Future Weather", "FutureWeather.futureWeather")

end DemoJsPage

object DemoJsPage:
  object Text extends DemoJsPage(scalatags.Text)
end DemoJsPage
