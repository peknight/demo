package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.Monad
import cats.data.State
import cats.effect.*
import cats.effect.std.Dispatcher
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.event.EventListenerOps.*
import com.peknight.demo.js.common.event.EventType.*
import com.peknight.demo.js.common.std.Color.{Blue, Green, Red}
import com.peknight.demo.js.common.std.Point
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.state.StateOps.*
import com.peknight.demo.js.common.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.{KeyCode, html}
import spire.implicits.*

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, DurationInt, DurationLong, FiniteDuration}
import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders:

  case class Runtime(player: Point[Double], enemies: Seq[Point[Double]], bullets: Seq[Point[Double]], wave: Int,
                     previousTime: Duration)

  def handlePlayer(player: Point[Double], keysDown: Set[Int], duration: Duration, width: Int, height: Int): Point[Double] =
    val length = duration.toMillis / 10
    keysDown.foldLeft(player){
      case (p, KeyCode.Left) => Point(if p.x > length then p.x - length else 0, p.y)
      case (p, KeyCode.Up) => Point(p.x, if p.y > length then p.y - length else 0)
      case (p, KeyCode.Right) =>
        val x = p.x + length
        Point(if x < width then x else width, p.y)
      case (p, KeyCode.Down) =>
        val y = p.y + length
        Point(p.x, if y < height then y else height)
      case (p, _) => p
    }

  def handleBullets(bullets: Seq[Point[Double]], addBullet: Boolean, player: Point[Double], duration: Duration)
  : Seq[Point[Double]] =
    val length = duration.toMillis / 4
    val addedBullets = if addBullet then player +: bullets else bullets
    addedBullets.map(p => Point(p.x, p.y - length)).filter(_.y >= 0)

  def handleWave(enemies: Seq[Point[Double]], wave: Int, width: Int): (Seq[Point[Double]], Int) =
    if enemies.nonEmpty then (enemies, wave) else (
      for
        x <- 0 until width by 50
        y <- 0 until wave
      yield Point(x, y * 50 + 50),
      wave + 1
    )

  def handleEnemies(enemies: Seq[Point[Double]], bullets: Seq[Point[Double]], previousTime: Duration,
                    currentTime: Duration, height: Int): Seq[Point[Double]] =
    val (_, x, y) = (previousTime.toSeconds to currentTime.toSeconds)
      .map(_.seconds)
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

  def next(runtime: Runtime, addBullet: Boolean, keysDown: Set[Int], currentTime: Duration, width: Int, height: Int)
  : Runtime =
    val duration = currentTime - runtime.previousTime
    val player = handlePlayer(runtime.player, keysDown, duration, width, height)
    val (waveEnemies, wave) = handleWave(runtime.enemies, runtime.wave, width)
    val bullets = handleBullets(runtime.bullets, addBullet, player, duration)
    val enemies = handleEnemies(waveEnemies, bullets, runtime.previousTime, currentTime, height)
    Runtime(player, enemies, bullets, wave, currentTime)

  def nextState[F[_]: Clock: Monad](addBulletR: Ref[F, Boolean], keysDownR: Ref[F, Set[Int]], width: Int, height: Int)
  : F[State[Runtime, Runtime]] =
    for
      addBullet <- addBulletR.getAndSet(false)
      keysDown <- keysDownR.get
      currentTime <- Clock[F].monotonic
    yield
      modifyAndGet[Runtime](next(_, addBullet, keysDown, currentTime, width, height))

  def draw[F[_]: Sync](canvas: html.Canvas, player: Point[Double], enemies: Seq[Point[Double]],
                       bullets: Seq[Point[Double]]): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- canvas.solid[F]()
      _ <- bullets.map(bullet =>
        renderer.drawSquare(Point.colored(bullet.x - 2, bullet.y - 2, Red), 4)).sequence.void
      _ <- enemies.map(enemy =>
        renderer.drawSquare(Point.colored(enemy.x - 5, enemy.y - 5, Green), 10)).sequence.void
      _ <- renderer.drawSquare(Point.colored(player.x - 5, player.y - 5, Blue), 10)
    yield ()

  def program[F[_]: Async](canvas: html.Canvas, rate: FiniteDuration): F[Unit] = Dispatcher.sequential[F].use {
    dispatcher =>
      given Dispatcher[F] = dispatcher
      for
        _ <- canvas.resize[F]
        addBulletR <- Ref.of[F, Boolean](false)
        keysDownR <- Ref.of[F, Set[Int]](Set())
        _ <- addEventListener(KeyPress) { case e if e.keyCode == KeyCode.Space => addBulletR.update(_ => true) }
        _ <- addEventListener(KeyDown) { e => keysDownR.update(_ + e.keyCode) }
        _ <- addEventListener(KeyUp) { e => keysDownR.update(_ - e.keyCode) }
        startTime <- Clock[F].monotonic
        runtime = Runtime(Point(canvas.width / 2, canvas.height / 2), Seq(), Seq(), 1, startTime)
        _ <- Stream.repeatEval(nextState(addBulletR, keysDownR, canvas.width, canvas.height)).through(state(runtime))
          .evalMap(r => draw(canvas, r.player, r.enemies, r.bullets)).metered(rate).compile.drain
      yield ()
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit = program[IO](canvas, 20.millis).run()
