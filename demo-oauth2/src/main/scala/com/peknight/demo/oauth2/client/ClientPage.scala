package com.peknight.demo.oauth2.client

import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.domain.WordsResult.*
import com.peknight.demo.oauth2.page.{OAuthPage, OAuthStyles}
import io.circe.Json
import org.http4s.Method.GET
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalacss.internal.Dsl.c
import scalatags.Text.all.{style as _, title as _, *}
import scalatags.Text.tags2.{nav, style, title}
import scalatags.generic.Frag
import scalatags.text.Builder

object ClientPage:

  def index(oauthTokenCache: OAuthTokenCache): Frag[Builder, String] = jumbotron(
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

  def error(error: String): Frag[Builder, String] = jumbotron(h2(cls := "text-danger")("Error"), error)

  def data(resource: Json): Frag[Builder, String] = jumbotron(h2("Data from protected resource:"), pre(resource.spaces4))

  def words(words: Seq[String], timestamp: Long, result: WordsResult): Frag[Builder, String] = skeleton(
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
  )

  def produce(scope: Set[String], data: ProduceData): Frag[Builder, String] = jumbotron(
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

  def favorites(data: UserFavoritesData): Frag[Builder, String] = jumbotron(
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

  private[this] def jumbotron(jumbotron: Modifier*): Frag[Builder, String] =
    OAuthPage.jumbotron("Client", "primary", c"#223")(jumbotron)

  private[this] def skeleton(main: Modifier*): Frag[Builder, String] =
    OAuthPage.skeleton("Client", "primary", c"#223")(main)


