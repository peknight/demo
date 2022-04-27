package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.Monad
import cats.effect.*
import cats.effect.std.Dispatcher
import cats.syntax.apply.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.dom.Point
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.stream.EventTopic.*
import com.peknight.demo.js.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.{KeyCode, html}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders:

  case class Runtime[F[_]](playerR: Ref[F, Point], enemiesR: Ref[F, Seq[Point]], bulletsR: Ref[F, Seq[Point]],
                           keysDownR: Ref[F, Set[Int]], waveR: Ref[F, Int], timeR: Ref[F, FiniteDuration])

  def movePlayer(player: Point, keysDown: Set[Int], duration: FiniteDuration): Point =
    val length = duration.toMillis / 10
    keysDown.foldLeft(player){
      case (p, keyCode) if keyCode == KeyCode.Left => Point(p.x - length, p.y)
      case (p, keyCode) if keyCode == KeyCode.Up => Point(p.x, p.y - length)
      case (p, keyCode) if keyCode == KeyCode.Right => Point(p.x + length, p.y)
      case (p, keyCode) if keyCode == KeyCode.Down => Point(p.x, p.y + length)
      case (p, _) => p
    }

  def moveBullets(bullets: Seq[Point], duration: FiniteDuration): Seq[Point] =
    val length = duration.toMillis / 4
    bullets.map(p => Point(p.x, p.y - length)).filter(_.y >= 0)

  def handleEnemyEmpty[F[_]: Monad](runtime: Runtime[F], width: Int): F[Seq[Point]] =
    for
      enemies <- runtime.enemiesR.get
      updatedEnemies <- if enemies.nonEmpty then Monad[F].pure(enemies) else
        for
          wave <- runtime.waveR.get
          es <- runtime.enemiesR.updateAndGet(_ =>
            for
              x <- 0 until width by 50
              y <- 0 until wave
            yield Point(x, 50 + y * 50))
          _ <- runtime.waveR.update(_ + 1)
        yield es
    yield updatedEnemies

  def moveEnemies(enemies: Seq[Point], bullets: Seq[Point], previousTime: FiniteDuration, currentTime: FiniteDuration,
                  height: Int) : Seq[Point] =
    val (_, x, y) = (previousTime.toSeconds to currentTime.toSeconds)
      .map(FiniteDuration(_, TimeUnit.SECONDS))
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
      .filter(enemy => !bullets.exists(bullet => enemy.length(bullet) < 5) && enemy.y <= height)

  def next[F[_]: Clock: Monad](runtime: Runtime[F], width: Int, height: Int): F[(Point, Seq[Point], Seq[Point])] =
    for
      previousTime <- runtime.timeR.get
      currentTime <- Clock[F].monotonic
      duration = currentTime - previousTime
      keysDown <- runtime.keysDownR.get
      _ <- handleEnemyEmpty(runtime, width)
      player <- runtime.playerR.updateAndGet(p => movePlayer(p, keysDown, duration))
      bullets <- runtime.bulletsR.updateAndGet(bs => moveBullets(bs, duration))
      enemies <- runtime.enemiesR.updateAndGet(es => moveEnemies(es, bullets, previousTime, currentTime, height))
      _ <- runtime.timeR.update(_ => currentTime)
    yield (player, enemies, bullets)

  def draw[F[_]: Sync](canvas: html.Canvas, player: Point, enemies: Seq[Point], bullets: Seq[Point]): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- canvas.clear[F]("black")
      _ <- renderer.drawSquare(Point.colored(player.x - 5, player.y - 5, "white"), 10)
      _ <- enemies.map(enemy =>
        renderer.drawSquare(Point.colored(enemy.x - 5, enemy.y - 5, "yellow"), 10)).sequence.void
      _ <- bullets.map(bullet =>
        renderer.drawSquare(Point.colored(bullet.x - 2, bullet.y - 2, "red"), 4)).sequence.void
    yield ()

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use { dispatcher =>
    for
      startTime <- Clock[F].monotonic
      // 变量定义
      runtime <- (Ref.of[F, Point](Point(canvas.width / 2, canvas.height / 2)), Ref.of[F, Seq[Point]](Seq()),
        Ref.of[F, Seq[Point]](Seq()), Ref.of[F, Set[Int]](Set()), Ref.of[F, Int](1),
        Ref.of[F, FiniteDuration](startTime)).mapN(Runtime.apply)

      // 监听事件
      keyPressTopic <- onKeyPressTopic(dispatcher)
      keyPressEvents = keyPressTopic.subscribe(16).filter(_.keyCode == KeyCode.Space).through(takeEvery(20.millis))
        .evalMap(e => runtime.playerR.get.flatMap(player => runtime.bulletsR.update(player +: _)))

      keyDownTopic <- onKeyDownTopic(dispatcher)
      keyDownEvents = keyDownTopic.subscribe(16).evalMap(e => runtime.keysDownR.update(_ + e.keyCode))

      keyUpTopic <- onKeyUpTopic(dispatcher)
      keyUpEvents = keyUpTopic.subscribe(16).evalMap(e => runtime.keysDownR.update(_ - e.keyCode))

      game = Stream.repeatEval(next(runtime, canvas.width, canvas.height)
        .flatMap(tuple => draw(canvas, tuple._1, tuple._2, tuple._3))).metered(20.millis)

      _ <- Stream(game, keyPressEvents, keyDownEvents, keyUpEvents).parJoin(4).compile.drain
    yield ()
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit = program[IO](canvas).run()
