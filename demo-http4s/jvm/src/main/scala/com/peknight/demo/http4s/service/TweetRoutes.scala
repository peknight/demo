package com.peknight.demo.http4s.service

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*

object TweetRoutes:
  case class Tweet(id: Int, message: String)

  given tweetEncoder: EntityEncoder[IO, Tweet] = ???
  given tweetsEncoder: EntityEncoder[IO, Seq[Tweet]] = ???

  def getTweet(tweetId: Int): IO[Tweet] = ???
  def getPopularTweets(): IO[Seq[Tweet]] = ???

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val tweetRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / "popular" => getPopularTweets().flatMap(Ok(_))
    case GET -> Root / "tweets" / IntVar(tweetId) => getTweet(tweetId).flatMap(Ok(_))
  }

