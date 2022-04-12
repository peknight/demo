package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.{IO, IOApp}
import fs2.Stream
import fs2.concurrent.Topic

object TopicApp extends IOApp.Simple {

  val run = for {
    _ <- Topic[IO, String].flatMap { topic =>
      val publisher = Stream.constant("1").covary[IO].through(topic.publish)
      val subscriber = topic.subscribe(10).take(4)
      subscriber.concurrently(publisher).compile.toVector
    } .flatMap(IO.println)
  } yield ()
}
