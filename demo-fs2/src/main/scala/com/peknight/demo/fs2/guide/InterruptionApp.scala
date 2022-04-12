package com.peknight.demo.fs2.guide

import cats.effect.{Clock, Deferred, IO, IOApp}
import fs2.Stream

import scala.concurrent.duration.DurationInt

object InterruptionApp extends IOApp.Simple {

  val program = Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
    val switcher = Stream.eval(switch.complete(())).delayBy(5.seconds)
    val program = Stream.repeatEval(Clock[IO].realTimeInstant.flatMap(IO.println)).metered(1.seconds)
    program.interruptWhen(switch.get.attempt).concurrently(switcher)
  }

  val program1 = Stream.repeatEval(Clock[IO].realTimeInstant.flatMap(IO.println))
    .metered(1.second)
    .interruptAfter(5.seconds)

  val interruptionRun = for {
    _ <- program.compile.drain
    _ <- IO.println("---")
    _ <- program1.compile.drain
  } yield ()

  case object Err extends Throwable

  val s1 = (Stream(1) ++ Stream(2)).covary[IO]

  val s2 = (Stream.empty ++ Stream.raiseError[IO](Err))
    // 这里可以加下delay看下三种情况
    .handleErrorWith { e => Stream.eval(IO.println(e)) >> Stream.raiseError[IO](e) }

  val merged = s1 merge s2 take 1

  val appendixesRun = for {
    _ <- IO.println((Stream(1) ++ Stream(2).map(_ => throw Err)).take(1).toList)
    _ <- IO.println("---")
    _ <- (Stream(1) ++ Stream.raiseError[IO](Err)).take(1).compile.toList.flatMap(IO.println)
    _ <- IO.println("---")
    _ <- Stream(1).covary[IO].onFinalize(IO.println("finalized!")).take(1).compile.toVector.flatMap(IO.println)
    _ <- IO.println("---")
    // 三种可能的情况: 1. s1先执行完，正常 2. s2先完整执行完，打印错误并抛出 3. s2出异常执行到handleErrorWith进行中时s1执行完，打印错误但返回正常
    _ <- merged.compile.toList.flatMap(IO.println)
  } yield ()


  val run = appendixesRun
}
