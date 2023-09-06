package com.peknight.demo.oauth2.page

import com.peknight.demo.oauth2.common.StringCaseStyle.camelToSnake
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.domain.WordsResult.*
import io.circe.Json
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.Method.{GET, POST}
import org.http4s.Uri
import org.http4s.syntax.literals.uri
import scalacss.internal.Dsl.c
import scalatags.generic.Bundle
import scalatags.text.Builder
import shapeless3.deriving.Labelling

class ClientPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT]) extends OAuthPage(bundle):
  import bundle.all.*

  def index(client: Option[ClientInfo], oauthTokenCache: OAuthTokenCache): Frag = jumbotron(
    p("Access token value: ", span(cls := "badge bg-danger")(oauthTokenCache.accessToken.getOrElse("NONE"))),
    p("Scope value: ", span(cls := "badge bg-danger")(oauthTokenCache.scope.map(_.mkString(" ")).getOrElse("NONE"))),
    p("Refresh token value: ", span(cls := "badge bg-danger")(oauthTokenCache.refreshToken.getOrElse("NONE"))),
    p("Access token key value: ", span(cls := "badge bg-danger")(oauthTokenCache.key
      .map(_.asJson.deepDropNullValues.noSpaces).getOrElse("NONE"))),
    p("Client ID: ", span(cls := "badge bg-danger")(client.map(_.id).getOrElse("None"))),
    p("Client Secret: ", span(cls := "badge bg-danger")(client.flatMap(_.secret).getOrElse("None"))),
    p("Registration access token: ", span(cls := "badge bg-danger")(client.flatMap(_.registrationAccessToken).getOrElse("None"))),
    p("Client configuration management endpoint: ", span(cls := "badge bg-danger")(
      client.flatMap(_.registrationClientUri).map(_.toString).getOrElse("None"))
    ),
    a(cls := "btn btn-outline-dark", href := "/authorize")("Get OAuth Token"), " ",
    a(cls := "btn btn-outline-dark", href := "/client_credentials")("Client Credentials"), " ",
    a(cls := "btn btn-outline-dark", href := "/username_password")("Username Password"), " ",
    a(cls := "btn btn-outline-dark", href := "/refresh")("Refresh Access Token"), " ",
    a(cls := "btn btn-outline-dark", href := "/fetch_resource")("Get Protected Resource"), " ",
    a(cls := "btn btn-outline-dark", href := "/words")("Access the Words API"), " ",
    a(cls := "btn btn-outline-dark", href := "/produce")("Access the Produce API"), " ",
    a(cls := "btn btn-outline-dark", href := "/favorites")("Access the Favorites API"), " ",
    a(cls := "btn btn-outline-dark", href := "/revoke")("Revoke Access Token Page"), " ",
    a(cls := "btn btn-outline-dark", href := "/userinfo")("Access User Info"), " ",
    form(action := "/revoke", cls := "form", method := POST.name)(
      input(`type` := "submit", cls := "btn btn-outline-dark", value := "Revoke Access Token")
    ),
    form(action := "/greeting")(
      input(cls := "btn btn-outline-dark", `type` := "submit", value := "Greet in"), " ",
      input(`type` := "radio", name := "language", value := "en", checked), "English", " ",
      input(`type` := "radio", name := "language", value := "de"), "German", " ",
      input(`type` := "radio", name := "language", value := "it"), "Italian", " ",
      input(`type` := "radio", name := "language", value := "fr"), "French", " ",
      input(`type` := "radio", name := "language", value := "es"), "Spanish"
    ),
    client.flatMap(_.registrationAccessToken).fold[Frag]("") { _ => frag(
      hr,
      a(cls := "btn btn-outline-dark", href := "/read_client")("Read Client Information"),
      form(method := POST.name, action := "/update_client")(
        label(`for` := "client_name")("New Client Name"), " ",
        input(`type` := "text", name := "client_name", value := client.flatMap(_.name).getOrElse("")),
        button(`type` := "submit", cls := "btn btn-outline-dark")("Update Client Registration")
      ),
      a(cls := "btn btn-outline-dark", href := "/unregister_client")("Unregister Client")
    )}
  )

  val webIndex: Frag = skeleton(
    div(cls := "bg-body-tertiary p-5 rounded")(
      p("Scope value: ", span(cls := s"badge bg-danger $oauthScopeValueCls")),
      p("Access token value: ", span(cls := s"badge bg-danger $oauthAccessTokenCls")),
      button(cls := s"btn btn-outline-dark $oauthAuthorizeCls", `type` := "button")("Get OAuth Token"), " ",
      button(cls := s"btn btn-outline-dark $oauthFetchResourceCls", `type` := "button")("Get Protected Resource"), " ",
      button(cls := s"btn btn-outline-dark $oauthGreetingCls", `type` := "button")("Greeting")
    ),
    div(cls := "bg-body-tertiary p-5 rounded")(
      h2("Data from protected resource:"),
      pre(span(cls := oauthProtectedResourceCls))
    )
  )(Some(uri"/main.js"))

  def error(error: String): Frag = jumbotron(h2(cls := "text-danger")("Error"), error)

  def data(resource: Json): Frag = jumbotron(h2("Data from protected resource:"), pre(resource.spaces4))

  def words(words: Seq[String], timestamp: Long, result: WordsResult): Frag = skeleton(
    div(cls := "row")(
      div(cls := "col-md-4")(
        div(cls := "well")(
          h3("Read the current value"),
          result match {
            case Get => p(span(cls := "badge bg-success")("Success"))
            case NoGet => p(span(cls := "badge bg-danger")("Failure"))
            case _ => ""
          },
          p(if words.nonEmpty then s"Words: ${words.mkString(" ")}" else ""),
          p(if timestamp > 0 then s"Timestamp: $timestamp" else ""),
          a(cls := "btn btn-info", href := "/get_words")("GET current value")
        )
      ),
      div(cls := "col-md-4")(
        div(cls := "well")(
          h3("Add a word to the list"),
          result match {
            case Add => p(span(cls := "badge bg-success")("Success"))
            case NoAdd => p(span(cls := "badge bg-danger")("Failure"))
            case _ => ""
          },
          form(cls := "form", action := "/add_word", method := GET.name)(
            input(`type` := "text", name := "word", placeholder := "word"), " ",
            input(`type` := "submit", cls := "btn btn-warning", href := "/", value := "POST a new word")
          )
        )
      ),
      div(cls := "col-md-4")(
        div(cls := "well")(
          h3("Remove the last word from the list"),
          result match {
            case Rm => p(span(cls := "badge bg-success")("Success"))
            case NoRm => p(span(cls := "badge bg-danger")("Failure"))
            case _ => ""
          },
          a(cls := "btn btn-danger", href := "/delete_word")("DELETE the last word")
        )
      )
    )
  )(None)

  def produce(scope: Set[String], data: ProduceData): Frag = jumbotron(
    h2("Produce API"),
    p("Current scope:", span(cls := "badge bg-info")(scope.mkString(" "))),
    p("Fruits:"),
    ul(data.fruit.map(li(_))),
    p("Veggies:"),
    ul(data.veggies.map(li(_))),
    p("Meats:"),
    ul(data.meats.map(li(_))),
    a(href := "/produce", cls := "btn btn-outline-dark")("Get Produce")
  )

  def favorites(data: UserFavoritesData): Frag = jumbotron(
    h2("Favorites API"),
    p(s"Resource owner's name: ${data.user}"),
    p("Movies:"),
    ul(data.favorites.movies.map(li(_))),
    p("Foods:"),
    ul(data.favorites.foods.map(li(_))),
    p("Music:"),
    ul(data.favorites.music.map(li(_))),
    a(href := "/favorites", cls := "btn btn-outline-dark")("Get Favorites")
  )

  def protectedResource(accessToken: String): Frag = jumbotron(
    "Access token value: ", span(cls := "badge bg-danger")(accessToken),
    a(cls := "btn btn-outline-dark", href := "/call_resource")("Get Protected Resource")
  )

  def revoke(oauthTokenCache: OAuthTokenCache): Frag = jumbotron(
    p("Access token value: ", span(cls := "badge bg-danger")(oauthTokenCache.accessToken.getOrElse("NONE"))),
    p("Scope value: ", span(cls := "badge bg-danger")(oauthTokenCache.scope.map(_.mkString(" ")).getOrElse("NONE"))),
    p("Refresh token value: ", span(cls := "badge bg-danger")(oauthTokenCache.refreshToken.getOrElse("NONE"))),
    form(action := "/revoke", cls := "form", method := POST.name)(
      a(cls := "btn btn-outline-dark", href := "/authorize")("Get OAuth Token"), " ",
      input(`type` := "submit", cls := "btn btn-outline-dark", value := "Revoke OAuthToken")
    )
  )

  def userInfo(userInfo: Option[UserInfo], idToken: Option[IdToken])(using labelling: Labelling[UserInfo]): Frag =
    jumbotron(
      p("Logged in user subject ", span(cls := "badge bg-info")(idToken.flatMap(_.subject).getOrElse("None")),
        " from issuer ", span(cls := "badge bg-warning")(idToken.flatMap(_.issuer).getOrElse("None")), "."),
      p("User information: ", userInfo.fold[Frag](span(cls := "badge bg-danger")("NOT FETCHED"))(user => ul(
        labelling.elemLabels.zip(Tuple.fromProductTyped(user).productIterator).map[Frag] {
          case (label, Some(value)) => li(b(camelToSnake(label)), s": $value")
          case _ => ""
        }
      ))),
      a(cls := "btn btn-outline-dark", href := "/authorize")("Log In"),
      a(cls := "btn btn-outline-dark", href := "/userinfo")("Get User Information")
    )

  val usernamePassword: Frag = jumbotron(
    h3("Get an access token"),
    form(cls := "form", action := "/username_password", method := POST.name)(
      input(`type` := "text", name := "username", placeholder := "username"), " ",
      input(`type` := "password", name := "password", placeholder := "password"), " ",
      input(`type` := "submit", cls := "btn btn-warning", href := "/", value := "Get an access token")
    )
  )

  private[this] def jumbotron(jumbotron: Modifier*): Frag =
    super.jumbotron("Client", "primary", c"#223")(jumbotron)

  private[this] def skeleton(main: Modifier*)(scriptSrc: Option[Uri]): Frag =
    super.skeleton("Client", "primary", c"#223")(main)(scriptSrc)

end ClientPage

object ClientPage:
  object Text extends ClientPage(scalatags.Text)
end ClientPage