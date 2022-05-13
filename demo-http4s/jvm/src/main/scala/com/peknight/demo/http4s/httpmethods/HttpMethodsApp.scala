package com.peknight.demo.http4s.httpmethods

import cats.effect.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.EntityEncoder.Pure
import org.http4s.circe.*
import org.http4s.dsl.io.*

object HttpMethodsApp:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def getTweet(tweetId: Int): IO[Option[TweetWithId]] = ???
  def addTweet(tweet: Tweet): IO[TweetWithId] = ???
  def updateTweet(id: Int, tweet: Tweet): IO[Option[TweetWithId]] = ???
  def deleteTweet(id: Int): IO[Unit] = ???

  given tweetWithIdEncoder: EntityEncoder[IO, TweetWithId] = jsonEncoderOf[TweetWithId]
  given tweetDecoder: EntityDecoder[IO, Tweet] = jsonOf[IO, Tweet]

  val tweetService = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / IntVar(tweetId) => getTweet(tweetId).flatMap(_.fold(NotFound())(Ok(_)))
    case req @ POST -> Root / "tweets" => req.as[Tweet].flatMap(addTweet).flatMap(Ok(_))
    case req @ PUT -> Root / "tweets" / IntVar(tweetId) =>
      req.as[Tweet].flatMap(updateTweet(tweetId, _)).flatMap(_.fold(NotFound())(Ok(_)))
    case HEAD -> Root / "tweets" / IntVar(tweetId) => getTweet(tweetId).flatMap(_.fold(NotFound())(_ => Ok()))
    case DELETE -> Root / "tweets" / IntVar(tweetId) => deleteTweet(tweetId).flatMap(_ => Ok())
  }
