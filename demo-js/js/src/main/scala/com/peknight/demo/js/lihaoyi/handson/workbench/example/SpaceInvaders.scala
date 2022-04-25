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
import com.peknight.demo.js.stream.StreamOps
import fs2.concurrent.Topic
import fs2.{Chunk, Stream}
import org.scalajs.dom
import org.scalajs.dom.{KeyCode, html}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders extends App:

  case class Runtime[F[_]](playerR: Ref[F, Point],
                           enemiesR: Ref[F, Seq[Point]],
                           bulletsR: Ref[F, Seq[Point]],
                           keysDownR: Ref[F, Set[Int]],
                           waveR: Ref[F, Int],
                           timeR: Ref[F, FiniteDuration])

  def eventTopic[F[_]: Async, E](dispatcher: Dispatcher[F])(update: (E => Unit) => Unit)
  : F[Topic[F, E]] =
    for
      topic <- Topic[F, E]
      _ <- Async[F].delay { update(e => dispatcher.unsafeToPromise(topic.publish1(e))) }
    yield topic

  def onKeyPressTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeypress = cb)

  def onKeyDownTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeydown = cb)

  def onKeyUpTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeyup = cb)

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
            yield Point(x, 50 + y * 50)
          )
          _ <- runtime.waveR.update(_ + 1)
        yield es
    yield updatedEnemies

  val directions = Map(
    KeyCode.Left -> Point(-2, 0),
    KeyCode.Up -> Point(0, -2),
    KeyCode.Right -> Point(2, 0),
    KeyCode.Down -> Point(0, 2)
  )

  def next[F[_]: Clock: Monad](runtime: Runtime[F], width: Int): F[(Point, Seq[Point], Seq[Point])] =
    for
      keysDown <- runtime.keysDownR.get
      player <- runtime.playerR.updateAndGet(p => directions.view.filterKeys(keysDown(_)).values
        .foldLeft(p)((p, d) => Point(p.x + d.x, p.y + d.y)))
      previousTime <- runtime.timeR.get
      currentTime <- Clock[F].monotonic
      duration = currentTime - previousTime
      bullets <- runtime.bulletsR.updateAndGet(_.map{p => Point(p.x, p.y - duration.toUnit(TimeUnit.MILLISECONDS) / 4)})
      _ <- handleEnemyEmpty(runtime, width)
      enemies <- runtime.enemiesR
        .updateAndGet(_.filter(enemy => !bullets.exists(bullet => enemy.length(bullet) < 5))
          .map(enemy =>
            val i = currentTime.toUnit(TimeUnit.SECONDS) % 4
            if i <= 0 then Point(enemy.x - 0.2, enemy.y)
            else if i <= 1 then Point(enemy.x, enemy.y + 0.2)
            else if i <= 2 then Point(enemy.x + 0.2, enemy.y)
            else Point(enemy.x, enemy.y + 0.2)
          ))
      _ <- runtime.timeR.update(_ => currentTime)
    yield (player, enemies, bullets)

  def draw[F[_]: Sync](canvas: html.Canvas, player: Point, enemies: Seq[Point], bullets: Seq[Point]): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- canvas.clear[F]("black")
      _ <- renderer.drawSquare(Point.colored(player.x - 5, player.y - 5, "white"), 10)
      _ <- enemies.map(enemy =>
        renderer.drawSquare(Point.colored(enemy.x - 5, enemy.y - 5, "yellow"), 10))
        .sequence.void
      _ <- bullets.map(bullet =>
        renderer.drawSquare(Point.colored(bullet.x - 2, bullet.y - 2, "red"), 4))
        .sequence.void
    yield ()

  def program[F[_]: Async](canvas: html.Canvas) = Dispatcher[F].use { dispatcher =>
    for
      startTime <- Clock[F].monotonic
      // 变量定义
      runtime <- (Ref.of[F, Point](Point(canvas.width / 2, canvas.height / 2)),
        Ref.of[F, Seq[Point]](Seq()),
        Ref.of[F, Seq[Point]](Seq()),
        Ref.of[F, Set[Int]](Set()),
        Ref.of[F, Int](1),
        Ref.of[F, FiniteDuration](startTime)).mapN(Runtime.apply)

      // 监听事件
      keyPressTopic <- onKeyPressTopic(dispatcher)
      keyPressEvents = keyPressTopic.subscribe(16)
        .filter(_.keyCode == KeyCode.Space)
        .through(StreamOps.takeEvery(20.millis))
        .evalMap(e => runtime.playerR.get.flatMap(player => runtime.bulletsR.update(player +: _)))

      keyDownTopic <- onKeyDownTopic(dispatcher)
      keyDownEvents = keyDownTopic.subscribe(16)
        .evalMap(e => runtime.keysDownR.update(_ + e.keyCode))

      keyUpTopic <- onKeyUpTopic(dispatcher)
      keyUpEvents = keyUpTopic.subscribe(16)
        .evalMap(e => runtime.keysDownR.update(_ - e.keyCode))

      game = Stream.repeatEval(next(runtime, canvas.width))
        .evalMap(tuple => draw(canvas, tuple._1, tuple._2, tuple._3)).metered(20.millis)

      // 整合流
      _ <- Stream(
        keyPressEvents,
        keyDownEvents,
        keyUpEvents,
        game
      ).parJoin(4).compile.drain
    yield ()
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit =
    program[IO](canvas).run()



