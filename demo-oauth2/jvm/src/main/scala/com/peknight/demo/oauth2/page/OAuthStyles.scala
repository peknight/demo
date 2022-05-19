package com.peknight.demo.oauth2.page

import scalacss.ProdDefaults.*
import scalacss.internal.ValueT
import scalacss.internal.ValueT.Color

case class OAuthStyles(navbarInverseBackgroundColor: ValueT[Color]) extends StyleSheet.Standalone :

  import dsl.*

  "body" - paddingTop(60.px)

  ".navbar-inverse" - backgroundColor(navbarInverseBackgroundColor)
