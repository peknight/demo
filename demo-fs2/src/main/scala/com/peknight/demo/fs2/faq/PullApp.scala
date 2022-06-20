package com.peknight.demo.fs2.faq

import cats.effect.{IO, IOApp}
import fs2.{Pull, Stream}

import scala.concurrent.duration.*

object PullApp extends IOApp.Simple:

  // Stream[F, O]: Evaluates effects in F, Emits 0..N values of O
  // Better for control flow because monadic over O: flatMap(f: A => Stream[F, B]): Stream[F, B]

  // Easy to "do this on each emitted value"

  // every second a new thread starts (with next number)
  // each thread screams for beer everything stops after 1 minute => 5 seconds
  val stream: Stream[IO, Unit] = Stream.iterate[IO, Int](1)(_ + 1)
    .metered(1.second)
    .map(n => scream1000Times(s"I want $n beers!"))
    .parJoinUnbounded
    .interruptAfter(5.seconds)

  def scream1000Times(e: String): Stream[IO, Unit] = Stream.repeatEval(IO.println(e)).take(1000)

  // Pull[F, O, R]: Evaluates effects in F, Emits 0..N values of O, Completes with a value of R
  // Better for streaming IO and stateful iterations because monadic over R:
  //   flatMap(f: R => Pull[F, A, R2]): Pull[F, A, R2]

  // Easy to "keep iterating"

  // Skip the first "skip" element
  val pull: List[String] = Stream("ok", "skip", "next ok", "skip", "told you").pull.takeWhile(o => o != "skip").flatMap {
    case None => Pull.pure(None)
    case Some(s) => s.drop(1).pull.echo
  }.void.stream.toList

  val run: IO[Unit] =
    for
      _ <- stream.compile.drain
      _ <- IO.println(pull)
    yield ()
