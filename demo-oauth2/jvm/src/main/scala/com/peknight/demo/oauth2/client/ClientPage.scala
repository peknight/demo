package com.peknight.demo.oauth2.client

import com.peknight.demo.oauth2.domain.OAuthTokenCache
import com.peknight.demo.oauth2.page.{OAuthPage, OAuthStyles}
import io.circe.Json
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalacss.internal.Dsl.c
import scalatags.Text.all.{style as _, title as _, *}
import scalatags.Text.tags2.{nav, style, title}
import scalatags.generic.Frag
import scalatags.text.Builder

object ClientPage:

  def index(oauthTokenCache: OAuthTokenCache): Frag[Builder, String] = skeleton(
    p("Access token value: ", span(cls := "label label-danger")(oauthTokenCache.accessToken.getOrElse("NONE"))),
    p("Scope value: ", span(cls := "label label-danger")(oauthTokenCache.scope.map(_.mkString(" ")).getOrElse("NONE"))),
    p("Refresh token value: ", span(cls := "label label-danger")(oauthTokenCache.refreshToken.getOrElse("NONE"))),
    a(cls := "btn btn-default", href := "/authorize")("Get OAuth Token"), " ",
    a(cls := "btn btn-default", href := "/fetch_resource")("Get Protected Resource")
  )

  def error(error: String): Frag[Builder, String] = skeleton(h2(cls := "text-danger")("Error"), error)

  def data(resource: Json): Frag[Builder, String] = skeleton(h2("Data from protected resource:"), pre(resource.spaces4))

  private[this] def skeleton(jumbotron: Modifier*): Frag[Builder, String] =
    OAuthPage.skeleton("Client", "primary", c"#223")(jumbotron)