package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*
// 或导入：import scalatags.Text.all.* 这个是生成html字符串的，而JsDom可以直接生成dom.Elements

object BasicExamples:
  val helloWorld = "<!DOCType html>" + html(
    head(script("some script")),
    body(
      h1("This is my title"),
      div(
        p("This is my first paragraph"),
        p("This is my second paragraph")
      )
    )
  )

  val attributesDemo = html(
    head(script("some script")),
    body(
      h1("This is my title"),
      div(
        p(onclick := "... do some js")("This is my first paragraph")
      ),
      a(href := "www.google.com")(p("Goooogle")),
      p(hidden)("I am hidden")
    )
  )

  val inlineStylesAndClassesDemo = html(
    head(script("some script")),
    body(
      h1(backgroundColor := "blue", color := "red")("This is my title"),
      // 等同于：
      // h1(style := "backgroundColor: blue; color: red;")("This is my title"),
      div(backgroundColor := "blue", color := "red")(
        p(cls := "contentpara first")("This is my first paragraph"),
        // 等同于：
        // p(`class` := "contentpara first")("This is my first paragraph"),
        a(opacity := "0.9")(p(cls := "contentpara")("Goooogle"))
      )
    )
  )

  // scalatags中未定义的属性可以用attr("...")代替，空属性可以用attr("...").empyty代替
  val customAttributesDemo = html(
    head(script("some script")),
    body(
      h1("This is my title"),
      div(
        p(attr("onclick") := "... do some js")("This is my first paragraph")
      ),
      a(attr("href") := "www.google.com")(p("Goooogle")),
      p(attr("hidden").empty)("I am hidden")
    )
  )

  // 使用css("...")创建自定义的inline style
  val customStyleDemo = div(
    attr("data-app-key") := "YOUR_APP_KEY",
    css("background-color") := "red"
  )

  val dataAppKey = attr("data-app-key")
  val customBackgroundColorStyle = css("background-color")
  val customStyleViaVariableDemo = div(
    dataAppKey := "YOUR_APP_KEY",
    customBackgroundColorStyle := "rec"
  )

  val customTagNamesDemo = tag("html")(
    tag("head")(tag("script")("some script")),
    tag("body")(
      tag("h1")("This is my title"),
      tag("p")("Hello")
    )
  )

  // val rssXml =
  //   val snippet = tag("rss")(attr("version") := "2.0")(tag("channel")(
  //     tag("title")("Haoyi's Programming Blog"),
  //     tag("link")("http://www.lihaoyi.com/"),
  //     tag("description"),
  //     for (name, rawHtmlContent, rawHtmlSnippet, updates) <- posts yield tag("item")(
  //       tag("title")(name),
  //       tag("link")(s"http://www.lihaoyi.com/post/${sanitize(name)}.html"),
  //       tag("description")(rawHtmlSnippet),
  //       for (sha, date) <- updates.lastOption yield tag("pubDate")(formatRssDate(date)),
  //       for (sha, date) <- updates.headOption yield tag("lastBuildDate")(formatRssDate(date))
  //     )
  //   ))
  //   """<?xml version="1.0"?>""" + snippet.render

  val nonStringAttributesAndStylesDemo = div(
    p(float.left)("This is my first paragraph"),
    a(tabindex := 10)(p("Goooogle")),
    input(disabled := true)
  )

  val appendPxAutomaticallyDemo = div(width := 100, zIndex := 100, padding:= 100)
  // 等同于 <div style="width: 100px; z-index: 100; padding: 100px;"></div> z-index不会受影响

  val booleanAttributesDemo1 = div(input(readonly))
  // 等同于 <div><input readonly="readonly" /></div>
  val booleanAttributesDemo2 = div(input(readonly := 1))
  // 等同于 <div><input readonly="1" /></div>

  val customTitle = "title"
  val numVisitors = 1023
  val variablesDemo = html(
    head(script("some script")),
    body(
      h1("This is my ", customTitle),
      div(
        p("This is my first paragraph"),
        p("you are the ", numVisitors.toString, "th visitor!")
      )
    )
  )

  val posts = Seq(
    ("alice", "i like pie"),
    ("bob", "pie is evil i hate you"),
    ("charlie", "i like pie and pie is evil, i hat myself")
  )
  val controlFlowDemo = html(
    head(script("some script")),
    body(
      h1("This is my title"),
      div("posts"),
      for (name, text) <- posts yield div(
        h2("Post by ", name),
        p(text)
      ),
      if numVisitors > 100 then p("No more posts!") else p("Please post below...")
    )
  )

  def imgBox(source: String, text: String) = div(img(src := source), div(p(text)))
  val functionsDemo = html(
    head(script("some script")),
    body(
      h1("This is my title"),
      imgBox("www.mysite.com/imageOne.png", "This is the first image displayed on the site"),
      div(`class` := "content")(
        p("blah blah blah i am text"),
        imgBox("www.mysite.com/imageTwo.png", "This image is very interesting")
      )
    )
  )

  val evilInput1 = "\"><script>alert('hello!')</script>"
  val evilInput2 = "<script>alert('hello!')</script>"
  val autoEscapingAndUnsanitizedInputDemo = html(
    head(script("some script")),
    body(
      h1(title := evilInput1, "This is my title"),
      evilInput2
    )
  )

  // 特殊需求确实需要使用这种有风险的字符串参数时可以用raw
  val rawInputDemo = html(
    head(script("some script")),
    body(
      h1("This is my title"),
      // 这里会变成一个脚本标签
      raw(evilInput2)
    )
  )

  def page(scripts: Seq[Modifier], content: Seq[Modifier]) = html(
    head(scripts),
    body(
      h1("This is my title"),
      div(cls := "content")(content)
    )
  )

  val layoutsDemo = page(Seq(script("some script")), Seq(
    p("This is the first ", b("image"), " displayed on the ", a("site")),
    img(src := "www.myImage.com/image.jpg"),
    p("blah blah blah i am text")
  ))

  class Parent:
    def render = html(headFrag, bodyFrag)
    def headFrag = head(script("some script"))
    def bodyFrag = body(
      h1("This is my title"),
      div(
        p("This is my first paragraph"),
        p("This is my second paragraph")
      )
    )

  object Child extends Parent:
    override def headFrag = head(script("some other script"))

  val inheritanceDemo = Child.render

  // Data attributes work by separating the sections of the attribute with `.` instead of `-`
  val dataDemo = div(
    id := "electriccars",
    data.columns := "3",
    data.index.number := "12314",
    data.parent := "cars",
    "..."
  )
  // 等同于 <div id="electriccars" data-columns="3" data-index-number="12314" data-parent="cars"></div>

  val ariaDemo = div(
    div(id := "ch1Panel", role := "tabpanel", aria.labelledby := "ch1Tab")("Chapter 1 content goes here"),
    div(id := "ch2Panel", role := "tabpanel", aria.labelledby := "ch2Tab")("Chapter 2 content goes here"),
    div(id := "quizPanel", role := "tabpanel", aria.labelledby := "quizTab")("Quiz content goes here"),
  )

  val singleFragDemo = div(h1("Hello"), p("World"))

  val children = Seq[Frag](h1("Hello"), p("World"))
  val seqFragDemo = div(children)

  // frag将不同的Frag组成一个Frag
  val childrenViaFrag: Frag = frag(h1("Hello"), p("World"))
  val fragDemoViaFrag = div(childrenViaFrag)

  val singleModifierDemo = div(cls := "my-bootstrap-class", color := "red")

  val mods = Seq[Modifier](cls := "my-bootstrap-class", color := "red")
  val seqModifierDemo = div(mods)

  // modifier将不同的Modifier组成一个Modifier
  val modsViaModifier: Modifier = modifier(cls := "my-bootstrap-class", color := "red")
  val modifierDemoViaModifier = div(modsViaModifier)

  /*
   * 可以选择性导入(scalatags.Text._ 或 scalatags.JsDom._)：
   * tags: common HTML tags
   * tags2: less common HTML tags
   * attrs: common HTML attributes
   * styles: common CSS styles
   * styles2: less common CSS styles
   * svgTags: SVG tags
   * svgAttrs: attributes only associated with SVG elements
   * DataConverters: convenient extensions (e.g. 10.px) to create the CSS datatypes
   *
   * all: this imports the contents of Tags, Attrs, Styles and DataConverters
   * short: this imports the contents of Tags and DataConverters, but aliases Attrs and Styles as *
   */
  {
    import scalatags.JsDom.short.*
    //noinspection DuplicatedCode
    val managingImportsDemo1 = div(
      p(*.color := "red", *.fontSize := 64.pt)("Big Red Text"),
      img(*.href := "www.imgur.com/picture.jpg")
    )
  }
  {
    import scalatags.JsDom.implicits.*
    import scalatags.JsDom.tags.*
    import scalatags.JsDom.{attrs as attr, styles as css, *}
    //noinspection DuplicatedCode
    val managingImportsDemo2 = div(
      p(css.color := "red", css.fontSize := 64.pt)("Big Red Text"),
      img(attr.href := "www.imgur.com/picture.jpg")
    )
  }
  {
    import scalatags.JsDom.*
    import styles2.pageBreakBefore
    import svgAttrs.stroke
    import svgTags.svg
    import tags2.address
    val managingImportsDemo3 = div(
      p(pageBreakBefore.always, "a long paragraph which should not be broken"),
      address("500 Memorial Drive, Cambridge MA"),
      svg(stroke := "#0000ff")
    )
  }

  import scalatags.*
  object CustomBundle extends Text.Cap with text.Tags with text.Tags2 with Text.Aggregate:
    object st extends Text.Cap with Text.Styles with Text.Styles2
    object at extends Text.Cap with Text.Attrs
  end CustomBundle
  import CustomBundle.*
  val customBundleDemo = html(
    head(script("some script")),
    body(
      h1(st.backgroundColor := "blue", st.color := "red")("This is my title"),
      div(st.backgroundColor := "blue", st.color := "red")(
        p(at.cls := "contentpara first")("This is my first paragraph"),
        a(st.opacity := 0.9)(p(at.cls := "contentpara")("Goooogle"))
      )
    )
  )





