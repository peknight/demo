package com.peknight.demo.frontend.vue.esm

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("/webjars/vue/3.4.27/dist/vue.esm-browser.prod.js", JSImport.Namespace)
// 启用导入映射表Import maps来告诉浏览器如何定位到导入的vue，这玩意chrome支持，firefox.v107及以前不支持或需手动配置支持
// @JSImport("vue", JSImport.Namespace)
object Vue extends com.peknight.demo.frontend.vue.common.Vue