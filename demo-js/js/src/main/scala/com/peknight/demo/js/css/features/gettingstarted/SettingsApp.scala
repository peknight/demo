package com.peknight.demo.js.css.features.gettingstarted

object SettingsApp:
  // 开发设置
  // import scalacss.DevDefaults.*
  // 生产设置
  // import scalacss.ProdDefaults.*
  // 基于`sbt -Dscalacss.mode=dev`或`sbt -Dscalacss.mode=prod`
  // 默认是dev-mode，除非是fullLinkJS的情况
  val CssSettings = scalacss.devOrProdDefaults
  import CssSettings.*
