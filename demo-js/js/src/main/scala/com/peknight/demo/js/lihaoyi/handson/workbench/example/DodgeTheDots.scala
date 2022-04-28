package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.effect.std.Dispatcher
import cats.effect.{Async, Clock, IO, Ref}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.dom.Point
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.state.StateOps.*
import com.peknight.demo.js.stream.EventTopic.*
import com.peknight.demo.js.stream.EventType.*
import fs2.Stream
import org.scalajs.dom.html

import scala.concurrent.duration.Duration
import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots:

  case class Enemy(pos: Point, vel: Point)

  case class Runtime(random: Random, player: Point, enemies: Seq[Point], death: Option[(String, Duration)],
                     startTime: Duration, previousTime: Duration)

  def reset(currentTime: Duration): State[Runtime, Runtime] = modifyAndGet { runtime =>
    val (death, startTime) = runtime.death match
      case Some((msg, remain)) =>
        val duration = currentTime - runtime.previousTime
        if remain < duration then (None, currentTime)
        else (Some(msg, remain - duration), runtime.startTime)
      case None => (None, runtime.startTime)
    runtime.copy(death = death, startTime = startTime)
  }

  def createEnemy(width: Int): State[Random, Enemy] =
    for
      x <- State[Random, Int](_.nextIntBounded(width))
      xSpeed <- State[Random, Double](_.between(-0.15, 0.1))
      ySpeed <- State[Random, Double](_.between(-0.15, 0.1))
    yield Enemy(Point(x, 0), Point(xSpeed, ySpeed))

  def createEnemies(size: Int, width: Int): State[Random, Seq[Enemy]] = Seq.fill(size)(createEnemy(width)).sequence

  def handleEnemies(random: Random, player: Point, enemies: Seq[Enemy], width: Int, height: Int): (Random, Seq[Enemy]) =
    val inBoundEnemies = enemies.filter(e => e.pos.x >= 0 && e.pos.x <= width && e.pos.y >= 0 && e.pos.y <= height)
    val (nextRandom, filledEnemies) =
      if inBoundEnemies.size < 20 then createEnemies(20 - inBoundEnemies.size, width).run(random).value
      else (random, inBoundEnemies)
    filledEnemies.map { enemy =>
      val delta = Point(player.x - enemy.pos.x, player.y - enemy.pos.y)
      ???
    }
    ???

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use { dispatcher =>
    for
      mouseMoveTopic <- eventTopic(MouseMove)(dispatcher)
      mouseMoveEvents = mouseMoveTopic.subscribe(1).map(e => modifyAndGet[Runtime](r =>
        r.copy(player = Point(e.clientX - canvas.clientLeft, e.clientY - canvas.clientTop))))

      startTime <- Clock[F].monotonic
      runtime = Runtime(Random(0), Point(canvas.width / 2, canvas.height / 2), Seq(), None, startTime, startTime)

      _ <- Stream(mouseMoveEvents).parJoin(2).compile.drain
    yield ()
  }

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = program[IO](canvas).run()

