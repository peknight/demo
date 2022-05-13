package com.peknight.demo.http4s.jsonhandling

import cats.effect.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
// 可以用替换下面两个import
// import org.http4s.circe.CirceEntityCodec.*
// 为每个A创建EntityDecoder[A]的解码实例。小心使用：这个隐式引入会导致http4s总是尝试使用json解码http实体（比如实体实际是XML或普通文本的情况）
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*


object JsonHandlingApp extends IOApp.Simple:

  def hello(name: String): Json = parse(s"""{"hello": $name}""").getOrElse(Json.Null)

  val greeting = hello("world")

  val alice = POST(parse(s"""{"name": "Alice"}""").getOrElse(Json.Null), uri"/hello")

  given helloEncoder: Encoder[Hello] = Encoder.forProduct1("hello")(h => h.name)

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
    yield ()
