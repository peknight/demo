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
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.random.Random
import com.peknight.demo.js.common.state.StateOps.*
import com.peknight.demo.js.common.std.Color.*
import com.peknight.demo.js.common.std.{Point, Vector}
import com.peknight.demo.js.common.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}
import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots:

  case class Enemy(pos: Point[Double], vel: Vector[Double])

  case class Runtime(random: Random, player: Point[Double], enemies: Seq[Enemy], death: Option[(String, Duration)],
                     startTime: Duration, previousTime: Duration)

  def createEnemy(width: Int): State[Random, Enemy] =
    for
      x <- State[Random, Int](_.nextIntBounded(width))
      xSpeed <- State[Random, Double](_.between(-0.15, 0.1))
      ySpeed <- State[Random, Double](_.between(-0.15, 0.1))
    yield Enemy(Point(x, 0), Vector(xSpeed, ySpeed))

  def createEnemies(size: Int, width: Int): State[Random, Seq[Enemy]] = Seq.fill(size)(createEnemy(width)).sequence

  def handleEnemies(random: Random, player: Point[Double], enemies: Seq[Enemy], previousTime: Duration,
                    currentTime: Duration, maxEnemies: Int, width: Int, height: Int): (Random, Seq[Enemy]) =
    val inBoundEnemies = enemies.filter(e => e.pos.x >= -20 && e.pos.x <= width + 20 && e.pos.y >= -20 &&
      e.pos.y <= height + 20)
    val (lastRandom, filledEnemies) =
      if inBoundEnemies.size < maxEnemies then
        val (nextRandom, createdEnemies) = createEnemies(maxEnemies - inBoundEnemies.size, width).run(random).value
        (nextRandom, createdEnemies ++ inBoundEnemies)
      else (random, inBoundEnemies)
    val movedEnemies = filledEnemies.map { enemy =>
      val toPlayer = player - enemy.pos
      val delta = enemy.vel * (currentTime - previousTime).toMillis.toDouble
      Enemy(enemy.pos + delta, enemy.vel + toPlayer / toPlayer.length / 2000)
    }
    (lastRandom, movedEnemies)

  def checkDeath(player: Point[Double], enemies: Seq[Enemy], death: Option[(String, Duration)], startTime: Duration,
                 currentTime: Duration): (Seq[Enemy], Option[(String, Duration)], Duration) = death match
    case None if enemies.exists(e => (e.pos - player).length < 20) =>
      (enemies, Some((s"You lasted ${(currentTime - startTime).toSeconds} seconds", currentTime + 2.seconds)), startTime)
    case Some((_, restartTime)) if currentTime >= restartTime =>
      (enemies.filter(e => (e.pos - player).length >= 20), None, currentTime)
    case _ => (enemies, death, startTime)

  def next(runtime: Runtime, player: Point[Double], currentTime: Duration, maxEnemies: Int, width: Int, height: Int)
  : Runtime =
    val (nextRandom, nextEnemies) = handleEnemies(runtime.random, player, runtime.enemies, runtime.previousTime,
      currentTime, maxEnemies, width, height)
    val (lastEnemies, lastDeath, nextStartTime) = checkDeath(player, nextEnemies, runtime.death, runtime.startTime,
      currentTime)
    Runtime(nextRandom, player, lastEnemies, lastDeath, nextStartTime, currentTime)

  def nextState[F[_]: Clock: Monad](playerR: Ref[F, Point[Double]], maxEnemies: Int, width: Int, height: Int)
  : F[State[Runtime, Runtime]] =
    for
      player <- playerR.get
      currentTime <- Clock[F].monotonic
    yield
      modifyAndGet[Runtime](next(_, player, currentTime, maxEnemies, width, height))

  def draw[F[_]: Sync](canvas: html.Canvas, player: Point[Double], enemies: Seq[Enemy],
                       death: Option[(String, Duration)], startTime: Duration, currentTime: Duration): F[Unit] =
    val renderer = canvas.context2d
    val delta = Vector[Double](10, 10)
    for
      _ <- canvas.solid[F]()
      _ <- death match
        case None =>
          for
            _ <- renderer.drawSquare((player - delta).colored(Blue), 20)
            _ <- renderer.withColor(Blue)(_.fillText("player", player.x - 15, player.y - 30))
            _ <- enemies.map(enemy => renderer.drawSquare((enemy.pos - delta).colored(Red), 20)).sequence
            _ <- renderer.withColor(Blue)(_.fillText(s"${(currentTime - startTime).toSeconds} seconds",
              canvas.width / 2 - 100, canvas.height / 5))
          yield ()
        case Some((msg, _)) => renderer.withColor(Blue)(_.fillText(msg, canvas.width / 2 - 100, canvas.height / 2))
    yield ()

  def program[F[_]: Async](canvas: html.Canvas, maxEnemies: Int, rate: FiniteDuration): F[Unit] =
    Dispatcher.sequential[F].use { dispatcher =>
      given Dispatcher[F] = dispatcher
      for
        _ <- canvas.resize
        playerR <- Ref.of[F, Point[Double]](Point(canvas.width / 2, canvas.height / 2))
        _ <- addEventListener(MouseMove)(e => playerR.update(_ =>
          Point(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop)))
        player <- playerR.get
        startTime <- Clock[F].monotonic
        runtime = Runtime(Random(startTime.toNanos), player, Seq(), None, startTime, startTime)
        _ <- Stream.repeatEval(nextState(playerR, maxEnemies, canvas.width, canvas.height))
          .through(state(runtime)).evalMap(r => draw(canvas, r.player, r.enemies, r.death, r.startTime, r.previousTime))
          .metered(rate).compile.drain
      yield ()
    }

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = program[IO](canvas, 20, 20.millis).run()

