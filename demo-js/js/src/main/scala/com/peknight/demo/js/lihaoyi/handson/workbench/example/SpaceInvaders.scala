package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.Functor
import cats.data.State
import cats.effect.*
import cats.effect.std.Dispatcher
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.dom.Point
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.state.StateOps.*
import com.peknight.demo.js.stream.EventTopic.*
import com.peknight.demo.js.stream.EventType.*
import com.peknight.demo.js.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.{KeyCode, html}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, DurationInt}
import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders:

  case class Runtime(player: Point, enemies: Seq[Point], bullets: Seq[Point], keysDown: Set[Int], wave: Int,
                     time: Duration, draw: Boolean = false)

  def handlePlayer(player: Point, keysDown: Set[Int], duration: Duration): Point =
    val length = duration.toMillis / 10
    keysDown.foldLeft(player){
      case (p, KeyCode.Left) => Point(p.x - length, p.y)
      case (p, KeyCode.Up) => Point(p.x, p.y - length)
      case (p, KeyCode.Right) => Point(p.x + length, p.y)
      case (p, KeyCode.Down) => Point(p.x, p.y + length)
      case (p, _) => p
    }

  def handleBullets(bullets: Seq[Point], duration: Duration): Seq[Point] =
    val length = duration.toMillis / 4
    bullets.map(p => Point(p.x, p.y - length)).filter(_.y >= 0)

  def handleWave(enemies: Seq[Point], wave: Int, width: Int): (Seq[Point], Int) =
    if enemies.nonEmpty then (enemies, wave) else (
      for
        x <- 0 until width by 50
        y <- 0 until wave
      yield Point(x, y * 50 + 50),
      wave + 1
    )

  def handleEnemies(enemies: Seq[Point], bullets: Seq[Point], previousTime: Duration, currentTime: Duration, height: Int)
  : Seq[Point] =
    val (_, x, y) = (previousTime.toSeconds to currentTime.toSeconds)
      .map(Duration(_, TimeUnit.SECONDS))
      .filter(sec => previousTime < sec && sec < currentTime)
      .appended(currentTime)
      .foldLeft((previousTime, 0.0, 0.0)) { case ((prev, x, y), current) =>
        val length = (current.toMicros - prev.toMicros).toDouble / 200000
        prev.toSeconds % 4 match
          case 0 => (current, x - length, y)
          case 2 => (current, x + length, y)
          case _ => (current, x, y + length)
      }
    enemies.map(enemy => Point(enemy.x + x, enemy.y + y))
      .filter(enemy => !bullets.exists(bullet => enemy.distance(bullet) < 5) && enemy.y <= height)

  def next(runtime: Runtime, currentTime: Duration, width: Int, height: Int): Runtime =
    val duration = currentTime - runtime.time
    val player = handlePlayer(runtime.player, runtime.keysDown, duration)
    val (waveEnemies, wave) = handleWave(runtime.enemies, runtime.wave, width)
    val bullets = handleBullets(runtime.bullets, duration)
    val enemies = handleEnemies(waveEnemies, bullets, runtime.time, currentTime, height)
    Runtime(player, enemies, bullets, runtime.keysDown, wave, currentTime, true)

  def nextState[F[_]: Clock: Functor](width: Int, height: Int): F[State[Runtime, Runtime]] =
    Clock[F].monotonic.map(currentTime => modifyAndGet[Runtime](next(_, currentTime, width, height)))

  def draw[F[_]: Sync](canvas: html.Canvas, player: Point, enemies: Seq[Point], bullets: Seq[Point]): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- canvas.clear[F]()
      _ <- bullets.map(bullet =>
        renderer.drawSquare(Point.colored(bullet.x - 2, bullet.y - 2, "red"), 4)).sequence.void
      _ <- enemies.map(enemy =>
        renderer.drawSquare(Point.colored(enemy.x - 5, enemy.y - 5, "green"), 10)).sequence.void
      _ <- renderer.drawSquare(Point.colored(player.x - 5, player.y - 5, "blue"), 10)
    yield ()

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use { dispatcher =>
    for
      keyPressTopic <- eventTopic(KeyPress)(dispatcher)
      keyPressEvents = keyPressTopic.subscribe(1).filter(_.keyCode == KeyCode.Space).through(takeEvery(20.millis))
        .map(e => modifyAndGet[Runtime](r => r.copy(bullets = r.player +: r.bullets)))

      keyDownTopic <- eventTopic(KeyDown)(dispatcher)
      keyDownEvents = keyDownTopic.subscribe(1)
        .map(e => modifyAndGet[Runtime](r => r.copy(keysDown = r.keysDown + e.keyCode)))

      keyUpTopic <- eventTopic(KeyUp)(dispatcher)
      keyUpEvents = keyUpTopic.subscribe(1)
        .map(e => modifyAndGet[Runtime](r => r.copy(keysDown = r.keysDown - e.keyCode)))

      process = Stream.repeatEval(nextState(canvas.width, canvas.height)).metered(20.millis)

      startTime <- Clock[F].monotonic
      runtime = Runtime(Point(canvas.width / 2, canvas.height / 2), Seq(), Seq(), Set(), 1, startTime)

      _ <- Stream(keyPressEvents, keyDownEvents, keyUpEvents, process)
        .parJoin(4)
        .through(state(runtime))
        .filter(_.draw)
        .evalMap(r => draw(canvas, r.player, r.enemies, r.bullets))
        .compile.drain
    yield ()
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit = program[IO](canvas).run()
