package com.peknight.demo.http4s.streaming

import cats.effect.*
import cats.syntax.functor.*
import fs2.Stream
import fs2.io.stdout
import fs2.text.{lines, utf8}
import io.circe.Json
import io.circe.jawn.CirceSupportParser
import org.http4s.*
import org.http4s.client.oauth1
import org.http4s.client.oauth1.ProtocolParameter.*
import org.http4s.ember.client.*
import org.http4s.implicits.*
import org.typelevel.jawn.Facade
import org.typelevel.jawn.fs2.*

class TWStream[F[_]: Async]:
  given Facade[Json] = new CirceSupportParser(None, false).facade

  def sign(consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String)
          (req: Request[F]): F[Request[F]] =
    val consumer = Consumer(consumerKey, consumerSecret)
    val token = Token(accessToken, accessSecret)
    oauth1.signRequest(
      req,
      consumer,
      Some(token),
      realm = None,
      timestampGenerator = Timestamp.now,
      nonceGenerator = Nonce.now
    )

  def jsonStream(consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String)
                (req: Request[F]): Stream[F, Json] =
    for
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      sr <- Stream.eval(sign(consumerKey, consumerSecret, accessToken, accessSecret)(req))
      res <- client.stream(sr).flatMap(_.body.chunks.parseJsonStream)
    yield res

  val stream: Stream[F, Unit] =
    val req = Request[F](Method.GET, uri"https://stream.twitter.com/1.1/statuses/sample.json")
    val s = jsonStream("<consumerKey>", "<consumerSecret>", "<accessToken>",
      "<accessSecret>")(req)
    s.map(_.spaces2).through(lines).through(utf8.encode).through(stdout)

  def run: F[Unit] = stream.compile.drain