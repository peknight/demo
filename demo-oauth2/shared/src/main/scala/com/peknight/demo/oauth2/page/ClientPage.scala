package com.peknight.demo.oauth2.page

import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.domain.WordsResult.*
import io.circe.Json
import org.http4s.Method.GET
import org.http4s.Uri
import org.http4s.syntax.literals.uri
import scalacss.internal.Dsl.c
import scalatags.generic.Bundle
import scalatags.text.Builder

class ClientPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT]) extends OAuthPage(bundle):
  import bundle.all.*

  def index(oauthTokenCache: OAuthTokenCache): Frag = jumbotron(
    p("Access token value: ", span(cls := "label label-danger")(oauthTokenCache.accessToken.getOrElse("NONE"))),
    p("Scope value: ", span(cls := "label label-danger")(oauthTokenCache.scope.map(_.mkString(" ")).getOrElse("NONE"))),
    p("Refresh token value: ", span(cls := "label label-danger")(oauthTokenCache.refreshToken.getOrElse("NONE"))),
    a(cls := "btn btn-default", href := "/authorize")("Get OAuth Token"), " ",
    a(cls := "btn btn-default", href := "/refresh")("Refresh Access Token"), " ",
    a(cls := "btn btn-default", href := "/fetch_resource")("Get Protected Resource"), " ",
    a(cls := "btn btn-default", href := "/words")("Access the Words API"), " ",
    a(cls := "btn btn-default", href := "/produce")("Access the Produce API"), " ",
    a(cls := "btn btn-default", href := "/favorites")("Access the Favorites API")
  )

  val webIndex: Frag = skeleton(
    div(cls := "jumbotron")(
      p("Scope value: ", span(cls := s"label label-danger $oauthScopeValueCls")),
      p("Access token value: ", span(cls := s"label label-danger $oauthAccessTokenCls")),
      button(cls := s"btn btn-default $oauthAuthorizeCls", `type` := "button")("Get OAuth Token"),
      button(cls := s"btn btn-default $oauthFetchResourceCls", `type` := "button")("Get Protected Resource")
    ),
    div(cls := "jumbotron")(
      h2("Data from protected resource:"),
      pre(span(cls := oauthProtectedResourceCls))
    )
  )(Some(uri"/webclient.js"))

  def error(error: String): Frag = jumbotron(h2(cls := "text-danger")("Error"), error)

  def data(resource: Json): Frag = jumbotron(h2("Data from protected resource:"), pre(resource.spaces4))

  def words(words: Seq[String], timestamp: Long, result: WordsResult): Frag = skeleton(
    div(cls := "row")(
      div(cls := "col-md-4")(
        div(cls := "well")(
          h3("Read the current value"),
          result match {
            case Get => p(span(cls := "label label-success")("Success"))
            case NoGet => p(span(cls := "label label-danger")("Failure"))
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
            case Add => p(span(cls := "label label-success")("Success"))
            case NoAdd => p(span(cls := "label label-danger")("Failure"))
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
            case Rm => p(span(cls := "label label-success")("Success"))
            case NoRm => p(span(cls := "label label-danger")("Failure"))
            case _ => ""
          },
          a(cls := "btn btn-danger", href := "/delete_word")("DELETE the last word")
        )
      )
    )
  )(None)

  def produce(scope: Set[String], data: ProduceData): Frag = jumbotron(
    h2("Produce API"),
    p("Current scope:", span(cls := "label label-info")(scope.mkString(" "))),
    p("Fruits:"),
    ul(data.fruit.map(li(_))),
    p("Veggies:"),
    ul(data.veggies.map(li(_))),
    p("Meats:"),
    ul(data.meats.map(li(_))),
    a(href := "/produce", cls := "btn btn-default")("Get Produce")
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
    a(href := "/favorites", cls := "btn btn-default")("Get Favorites")
  )

  private[this] def jumbotron(jumbotron: Modifier*): Frag =
    super.jumbotron("Client", "primary", c"#223")(jumbotron)

  private[this] def skeleton(main: Modifier*)(scriptSrc: Option[Uri]): Frag =
    super.skeleton("Client", "primary", c"#223")(main)(scriptSrc)

end ClientPage

object ClientPage:
  object Text extends ClientPage(scalatags.Text)
end ClientPage