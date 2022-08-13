package com.peknight.demo.js.lihaoyi.scalatags

/**
 * Cross-backend Code
 */
class SharedTemplates[Builder, Output <: FragT, FragT](val bundle: scalatags.generic.Bundle[Builder, Output, FragT]):
  import bundle.all.*
  val widget: Tag = div("hello")
end SharedTemplates

object SharedTemplates:
  object JsTemplates extends SharedTemplates(scalatags.JsDom)
  object TextTemplates extends SharedTemplates(scalatags.Text)
end SharedTemplates




