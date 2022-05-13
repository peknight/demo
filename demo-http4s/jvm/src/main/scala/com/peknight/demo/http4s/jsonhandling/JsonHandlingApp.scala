package com.peknight.demo.http4s.jsonhandling

import cats.effect.*
import com.peknight.demo.http4s.logEmberServer
import fs2.Stream
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.*
import org.http4s.implicits.*


object JsonHandlingApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def hello(name: String): Json = parse(s"""{"hello": $name}""").getOrElse(Json.Null)

  val greeting = hello("world")

  val alice = POST(parse(s"""{"name": "Alice"}""").getOrElse(Json.Null), uri"/hello")

  given helloEncoder: Encoder[Hello] = Encoder.forProduct1("hello")(h => h.name)
  given helloDecoder: Decoder[Hello] = Decoder.forProduct1("hello")(n => Hello(n))

  given userDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]

  // 为每个A创建EntityDecoder[A]的解码实例。小心使用：这个隐式引入会导致http4s总是尝试使用json解码http实体（比如实体实际是XML或普通文本的情况）
  // import org.http4s.circe.CirceEntityDecoder.*
  // 在仅提供json服务的router中，可以选择引入CirceEntityEncoder，引入后不再需要在结果上手动调用asJson。不过当我们这也可能带来模棱两可的错误Thus there's no more need in calling asJson on result. However, it may introduce ambiguity errors when we also build some json by hand within the same scope.
  // import org.http4s.circe.CirceEntityEncoder.*
  // 可以用Codec替换上面两个import
  // import org.http4s.circe.CirceEntityCodec.*

  val jsonApp = HttpRoutes.of[IO] {
    case req @ POST -> Root / "hello" =>
      for
        user <- req.as[User]
        resp <- Ok(Hello(user.name).asJson)
      yield resp
  }.orNotFound

  val server = logEmberServer[IO](jsonApp)

  def helloClient(name: String): Stream[IO, Hello] =
    val req = POST(User(name).asJson, uri"http://localhost:8080/hello")
    Stream.resource(EmberClientBuilder.default[IO].build).flatMap { httpClient =>
      Stream.eval(httpClient.expect(req)(using jsonOf[IO, Hello]))
    }

  val run =
    for
      greetingResp <- Ok(greeting)
      _ <- IO.println(greetingResp)
      _ <- IO.println(alice)
      _ <- IO.println(Hello("Alice").asJson)
      _ <- IO.println(User("Alice").asJson)
      helloAlice <- Ok(Hello("Alice").asJson)
      _ <- IO.println(helloAlice)
      _ <- IO.println(POST(User("Bob").asJson, uri"/hello"))
      aliceJson <- Ok("""{"name":"Alice"}""").flatMap(_.as[Json])
      _ <- IO.println(aliceJson)
      bobJson <- POST("""{"name":"Bob"}""", uri"/hello").as[Json]
      _ <- IO.println(bobJson)
      aliceUser <- Ok("""{"name":"Alice"}""").flatMap(_.as[User])
      _ <- IO.println(aliceUser)
      bobUser <- POST("""{"name":"Bob"}""", uri"/hello").as[User]
      _ <- IO.println(bobUser)
      serverAllocated <- server.allocated
      shutdown = serverAllocated._2
      helloAlice = helloClient("Alice")
      hello <- helloAlice.compile.last
      _ <- IO.println(hello)
      _ <- shutdown
    yield ()
