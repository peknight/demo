package com.peknight.demo.oauth2.authorizationserver

import com.peknight.demo.oauth2.domain.{AuthServerInfo, ClientInfo}
import com.peknight.demo.oauth2.page.{OAuthPage, OAuthStyles}
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalacss.internal.Dsl.c
import scalatags.Text.all.{style as _, title as _, *}
import scalatags.Text.tags2.{nav, style, title}
import scalatags.generic.Frag
import scalatags.text.Builder

object AuthorizationServerPage:

  def index(authServer: AuthServerInfo, clients: Seq[ClientInfo]): Frag[Builder, String] =
    val clientElements = for client <- clients yield
      ul(
        li(b("client_id: "), code(client.id)),
        li(b("client_secret: "), code(client.secret)),
        li(b("scope: "), code(client.scope.mkString(" "))),
        li(b("redirect_uris: "), code(client.redirectUris.toList.map(_.toString)))
      )

    skeleton(
      h2("Client information:"),
      clientElements,
      h2("Server information:"),
      ul(
        li(b("authorization_endpoint: "), code(authServer.authorizationEndpoint.toString)),
        li(b("token_endpoint: "), code(authServer.tokenEndpoint.toString))
      )
    )
  end index

  def error(error: String): Frag[Builder, String] = skeleton(h2(cls := "text-danger")("Error"), error)

  def approve(client: ClientInfo, reqId: String, scopes: List[String]): Frag[Builder, String] =
    skeleton(
      h2("Approve this client?"),
      client.name.fold[Modifier]("")(name => p(b("Name: "), code(name))),
      p(b("ID: "), code(client.id)),
      client.uri.fold[Modifier]("")(uri => p(b("URI: "), code(uri))),
      client.logoUri.fold[Modifier]("")(logoUri => p(b("logo: "), img(src := logoUri))),
      form(cls := "form", action := "/approve", method := "POST")(
        input(`type` := "hidden", name := "reqid", value := reqId),
        if scopes.isEmpty then ""
        else frag(
          p("The client is requesting access to the following:"),
          ul(
            for scope <- scopes yield
              li(
                input(`type` := "checkbox", name := s"scope_$scope", id := s"scope_$scope", checked := "checked"),
                " ",
                label(`for` := s"scope_$scope")(scope)
              )
          )
        ),
        input(`type` := "submit", cls := "btn btn-success", name := "approve", value := "Approve"),
        " ",
        input(`type` := "submit", cls := "btn btn-danger", name := "deny", value := "Deny")
      )
    )
  end approve

  private[this] def skeleton(jumbotron: Modifier*): Frag[Builder, String] =
    OAuthPage.jumbotron("Authorization Server", "danger", c"#322")(jumbotron)
