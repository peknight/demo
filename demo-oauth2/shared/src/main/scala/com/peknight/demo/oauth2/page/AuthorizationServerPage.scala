package com.peknight.demo.oauth2.page

import com.peknight.demo.oauth2.domain.*
import org.http4s.Method.POST
import scalacss.internal.Dsl.c
import scalatags.generic.Bundle
import scalatags.text.Builder

class AuthorizationServerPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends OAuthPage(bundle):
  import bundle.all.*

  def index(authServer: AuthServerInfo, clients: Seq[ClientInfo]): Frag =
    val clientElements = for client <- clients yield
      ul(
        li(b("client_id: "), code(client.id)),
        li(b("client_secret: "), code(client.secret.getOrElse("None"))),
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

  def error(error: String): Frag = skeleton(h2(cls := "text-danger")("Error"), error)

  def approve(client: ClientInfo, reqId: String, scopes: List[String]): Frag =
    skeleton(
      h2("Approve this client?"),
      client.name.fold[Modifier]("")(name => p(b("Name: "), code(name))),
      p(b("ID: "), code(client.id)),
      client.uri.fold[Modifier]("")(uri => p(b("URI: "), code(uri.toString))),
      client.logoUri.fold[Modifier]("")(logoUri => p(b("logo: "), img(src := logoUri.toString))),
      form(cls := "form", action := "/approve", method := POST.name)(
        label("Select user:"),
        select(name := "user")(
          option(value := "alice")("Alice"),
          option(value := "bob")("Bob")
        ),
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

  private[this] def skeleton(jumbotron: Modifier*): Frag =
    super.jumbotron("Authorization Server", "danger", c"#322")(jumbotron)

end AuthorizationServerPage

object AuthorizationServerPage:
  object Text extends AuthorizationServerPage(scalatags.Text)
end AuthorizationServerPage
