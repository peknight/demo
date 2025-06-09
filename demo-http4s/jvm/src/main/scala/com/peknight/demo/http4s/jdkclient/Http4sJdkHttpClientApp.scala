package com.peknight.demo.http4s.jdkclient

import cats.effect.*
import cats.implicits.*
import cats.syntax.parallel.*
import fs2.Stream
import org.http4s.*
import org.http4s.client.Client
import org.http4s.client.websocket.*
import org.http4s.implicits.*
import org.http4s.jdkhttpclient.*

import java.net.http.HttpClient
import java.net.{InetSocketAddress, ProxySelector}
import scala.concurrent.duration.*

object Http4sJdkHttpClientApp extends IOApp.Simple:

  val client: Resource[IO, Client[IO]] = JdkHttpClient.simple[IO]

  // Custom clients

  val client0: Resource[IO, Client[IO]] = Resource.eval(IO {
    HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .proxy(ProxySelector.of(new InetSocketAddress("www-proxy", 8080)))
      .build()
  }).flatMap(JdkHttpClient(_))

  // Sharing

  // client可以复用
  def fetchStatus[F[_]](c: Client[F], uri: Uri): F[Status] =
    c.status(Request[F](Method.GET, uri = uri))

  // Websocket client

  val httpWebSocketTupleIO: IO[(Client[IO], WSClient[IO])] = Resource.eval(IO(HttpClient.newHttpClient()))
    .flatMap(httpClient => (JdkHttpClient[IO](httpClient), JdkWSClient[IO](httpClient)).tupled)
    // in almost all cases, it is better to call `use` instead
    .allocated.map(_._1)

  val run =
    for
      // status <- client.use(c => fetchStatus(c, uri"https://http4s.org/"))
      // _ <- IO.println(status)
      httpWebSocketTuple <- httpWebSocketTupleIO
      (http, webSocket) = httpWebSocketTuple
      stream = Stream.awakeEvery[IO](1.second).zipRight(Stream(WSFrame.Text("hello1"), WSFrame.Text("hello2"), WSFrame.Text("hello3")))
      x <- webSocket.connectHighLevel(WSRequest(uri"wss://local.peknight.com:8080/ws")).use {
        connection =>
          (
            stream.evalMap(connection.send).compile.drain,
            connection.receiveStream.evalTap(frame => IO.println(s"receive: $frame")).take(3).compile.toList,
          ).parTupled
      }
    yield
      ()






