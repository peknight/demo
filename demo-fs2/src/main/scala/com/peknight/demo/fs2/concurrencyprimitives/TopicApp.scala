package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.{IO, IOApp}
import fs2.Stream
import fs2.concurrent.Topic

object TopicApp extends IOApp.Simple:

  val run =
    for
      _ <- Topic[IO, Int].flatMap { topic =>
        val publisher = Stream.iterate(1)(_ + 1).covary[IO]
          .evalMap(value => IO.println(s"publisher: $value").as(value))
          .through(topic.publish)
        val subscriber = topic.subscribe(10).take(4)
          .evalMap(value => IO.println(s"subscriber: $value").as(value))
        subscriber.concurrently(publisher).compile.toVector
      } .flatMap(IO.println)
    yield ()
