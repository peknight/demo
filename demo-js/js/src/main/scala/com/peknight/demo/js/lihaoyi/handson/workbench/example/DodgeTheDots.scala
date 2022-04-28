package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.Functor
import cats.data.State
import cats.effect.*
import cats.effect.std.Dispatcher
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.dom.Color.*
import com.peknight.demo.js.dom.{Point, Vector}
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.state.StateOps.*
import com.peknight.demo.js.stream.EventTopic.*
import com.peknight.demo.js.stream.EventType.*
import com.peknight.demo.js.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.{Duration, DurationInt}
import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots:

  case class Enemy(pos: Point[Double], vel: Vector[Double])

  case class Runtime(random: Random, player: Point[Double], enemies: Seq[Enemy],
                     death: Option[(String, Duration)], startTime: Duration, previousTime: Duration,
                     draw: Boolean = false)

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
    yield Enemy(Point(x, 0), Vector(xSpeed, ySpeed))

  def createEnemies(size: Int, width: Int): State[Random, Seq[Enemy]] = Seq.fill(size)(createEnemy(width)).sequence

  def handleEnemies(currentTime: Duration, width: Int, height: Int): State[Runtime, Runtime] = modifyAndGet { runtime =>
    val inBoundEnemies = runtime.enemies.filter(e => e.pos.x >= 0 && e.pos.x <= width && e.pos.y >= 0 && e.pos.y <= height)
    val (nextRandom, filledEnemies) =
      if inBoundEnemies.size < 20 then createEnemies(20 - inBoundEnemies.size, width).run(runtime.random).value
      else (runtime.random, inBoundEnemies)
    val movedEnemies = filledEnemies.map { enemy =>
      val delta = runtime.player - enemy.pos
      val vec = enemy.vel * (currentTime - runtime.previousTime).toMillis.toDouble
      Enemy(enemy.pos + vec, vec + delta / delta.length / 100)
    }
    runtime.copy(random = nextRandom, enemies = movedEnemies)
  }

  def checkDeath(currentTime: Duration): State[Runtime, Runtime] = modifyAndGet { runtime =>
    runtime.death match
      case None =>
        if runtime.enemies.exists(e => (e.pos - runtime.player).length < 20) then
          val nextEnemies = runtime.enemies.filter(e => (e.pos - runtime.player).length > 20)
          val nextDeath = Some((s"You lasted ${(currentTime - runtime.startTime).toSeconds} seconds", 2.seconds))
          runtime.copy(enemies = nextEnemies, death = nextDeath)
        else runtime
      case _ => runtime
  }

  def nextState[F[_]: Clock: Functor](width: Int, height: Int): F[State[Runtime, Runtime]] =
    Clock[F].monotonic.map(currentTime =>
      for
        _ <- reset(currentTime)
        _ <- handleEnemies(currentTime, width, height)
        _ <- checkDeath(currentTime)
        runtime <- modifyAndGet[Runtime](r => r.copy(previousTime = currentTime, draw = true))
      yield runtime
    )

  def draw[F[_]: Sync](canvas: html.Canvas, runtime: Runtime): F[Unit] =
    val renderer = canvas.context2d
    val delta = Vector[Double](10, 10)
    for
      _ <- canvas.clear[F](Black.value)
      _ <- runtime.death match
        case None =>
          for
            _ <- renderer.drawSquare((runtime.player - delta).colored(White), 20)
            _ <- runtime.enemies.map(enemy => renderer.drawSquare((enemy.pos - delta).colored(Red), 20)).sequence
            _ <- renderer.withFillStyle(White.value)(_.fillText(
              s"${(runtime.previousTime - runtime.startTime).toSeconds} seconds",
              canvas.width / 2 - 100, canvas.height / 5))
          yield ()
        case Some((msg, _)) => renderer.withFillStyle(White.value)(_.fillText(
          msg, canvas.width / 2 - 100, canvas.height / 2
        ))
    yield ()

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use { dispatcher =>
    for
      mouseMoveTopic <- eventTopic(MouseMove)(dispatcher)
      mouseMoveEvents = mouseMoveTopic.subscribe(1).through(takeEvery(5.millis))
        .map(e => modifyAndGet[Runtime](r => r.copy(
          player = Point(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop),
          draw = false)))

      process = Stream.repeatEval(nextState(canvas.width, canvas.height)).metered(20.millis)
      startTime <- Clock[F].monotonic
      runtime = Runtime(Random(0), Point(canvas.width / 2, canvas.height / 2), Seq(), None, startTime, startTime)

      _ <- Stream(mouseMoveEvents, process)
        .parJoin(2)
        .through(state(runtime))
        .filter(_.draw)
        .evalMap(r => draw(canvas, r))
        .compile.drain
    yield ()
  }

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = program[IO](canvas).run()

