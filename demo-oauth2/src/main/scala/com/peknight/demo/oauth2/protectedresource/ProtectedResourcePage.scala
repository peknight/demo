package com.peknight.demo.oauth2.protectedresource

import com.peknight.demo.oauth2.page.{OAuthPage, OAuthStyles}
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalacss.internal.Dsl.c
import scalatags.Text.all.{style as _, title as _, *}
import scalatags.Text.tags2.{nav, style, title}
import scalatags.generic.Frag
import scalatags.text.Builder

object ProtectedResourcePage:

  val index: Frag[Builder, String] =
    OAuthPage.skeleton("Protected Resource", "success", c"#232")(
      "To access the resource, send a ", code("POST"), " request to ",
      code("http://localhost:8002/resource"), " and include a valid OAuth token."
    )