package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.Concurrent
import cats.effect.std.{Console, Queue}
import fs2.Stream

class Buffering[F[_]: {Concurrent, Console}](q1: Queue[F, Int], q2: Queue[F, Int]):
  def start: Stream[F, Unit] = Stream(
    Stream.range(0, 1000).covary[F].foreach(q1.offer),
    Stream.repeatEval(q1.take).foreach(q2.offer),
    // .map won't work here as you're trying to map a pure value with a side effect
    Stream.repeatEval(q2.take).foreach(n => Console[F].println(s"Pulling out $n from Queue #2"))
  ).parJoin(3)