package com.peknight.demo.frontend.professional.form.richtext

import scalatags.generic.Bundle

class RichTextPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as inlineStyle, *}
  import bundle.tags2.{nav, section, style, title}

  val blank: Frag =
    html(
      head(title("Blank Page for Rich Text Editing")),
      body()
    )

  val iframeIndex: Frag =
    html(
      head(
        title("Rich Text - iframe"),
        script(`type` := "text/javascript", src := "/main.js"),
      ),
      body(
        iframe(name := "richedit", inlineStyle := "height:100px;width:100px;", src := "/rich-text-blank.htm"),
        script("richEditIframe()")
      )
    )

  val contentEditableIndex: Frag =
    html(
      head(
        title("Rich Text - contenteditable"),
        script(`type` := "text/javascript", src := "/main.js"),
      ),
      body(
        div(cls := "editable", id := "richedit", contenteditable := false),
        script("richEditContentEditable()")
      )
    )


end RichTextPage
object RichTextPage:
  object Text extends RichTextPage(scalatags.Text)
end RichTextPage

