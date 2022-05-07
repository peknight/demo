package com.peknight.demo.js.lihaoyi.handson.canvas

import cats.Monad
import cats.data.State
import cats.effect.*
import cats.effect.std.Dispatcher
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.event.EventListenerOps.*
import com.peknight.demo.js.common.event.EventType.*
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.state.StateOps.*
import com.peknight.demo.js.common.std.Color.{DarkBlue, DarkGreen, DarkRed}
import com.peknight.demo.js.common.std.Point
import com.peknight.demo.js.common.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.collection.immutable.Queue
import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}
import scala.scalajs.js.annotation.JSExportTopLevel

object FlappyBox:

  type PlayerY = Double
  type PlayerV = Double
  type Frame = Int

  object Config:
    val obstacleGap = 200
    val holeSize = 50
    val gravity = 0.0005

  import Config.*

  case class Runtime(random: Random,
                     // 玩家纵坐标（横坐标固定）
                     playerY: PlayerY,
                     // 玩家纵向速度
                     playerV: PlayerV,
                     // 标记第一个管道的位置
                     frame: Frame,
                     // 管道的洞的纵坐标的队列
                     obstacles: Queue[Int],
                     // 挂了：Some(重开时间)，没挂：None
                     dead: Option[Duration],
                     // 上一帧运行时间
                     previousTime: Duration)

  def handlePlayer(playerY: PlayerY, playerV: PlayerV, clickCount: Int, duration: Duration): (PlayerY, PlayerV) =
    val v = if clickCount > 0 then -0.16 else playerV
    val millis = duration.toMillis
    val d = v * millis + gravity * millis.pow(2) / 2
    (playerY + d, v + gravity * millis)

  def handleObstacles(random: Random, frame: Frame, obstacles: Queue[Int], duration: Duration, height: Int)
  : (Random, Frame, Queue[Int]) =
    val nextFrame = frame + (duration.toMillis / 10).toInt
    val (nextRandom, nextObstacles) =
      if nextFrame >= 0 && obstacles.length * obstacleGap <= nextFrame then
        val (rnd, holeY) = random.between(holeSize, height - holeSize)
        (rnd, obstacles.enqueue(holeY))
      else
        (random, obstacles)
    val (lastObstacles, lastFrame) =
      if nextObstacles.length > 7 then
        (nextObstacles.dequeue._2, nextFrame - obstacleGap)
      else (nextObstacles, nextFrame)
    println(lastObstacles)
    (nextRandom, lastFrame, lastObstacles)

  def getHoles(frame: Frame, obstacles: Queue[Int], width: Int): Queue[(Int, Int)] =
    obstacles.zipWithIndex.map { case (holeY, i) =>
      val holeX = i * obstacleGap - frame + width
      (holeX, holeY)
    }

  def checkDead(playerY: PlayerY, holes: Queue[(Int, Int)], currentTime: Duration, width: Int, height: Int)
  : Option[Duration] =
    if playerY < 0 || playerY > height then Some(currentTime + 1.second)
    else if holes.exists { case (holeX, holeY) => (holeX - width / 2).abs < 5 && (holeY - playerY).abs > holeSize } then
      Some(currentTime + 1.second)
    else None

  def handleDead(runtime: Runtime, currentTime: Duration, height: Int): Runtime =
    if runtime.dead.exists(_ <= currentTime) then
      Runtime(runtime.random, height / 2, 0, -50, Queue(), None, currentTime)
    else runtime

  def handleLive(runtime: Runtime, clickCount: Int, currentTime: Duration, width: Int, height: Int): Runtime =
    val duration = currentTime - runtime.previousTime
    val (playerY, playerV) = handlePlayer(runtime.playerY, runtime.playerV, clickCount, duration)
    val (random, frame, obstacles) = handleObstacles(runtime.random, runtime.frame, runtime.obstacles, duration, height)
    val holes = getHoles(frame, obstacles, width)
    val dead = checkDead(playerY, holes, currentTime, width, height)
    Runtime(random, playerY, playerV, frame, obstacles, dead, currentTime)

  def nextState[F[_]: Clock: Monad](clickCountR: Ref[F, Int], width: Int, height: Int): F[State[Runtime, Runtime]] =
    for
      clickCount <- clickCountR.getAndSet(0)
      currentTime <- Clock[F].monotonic
    yield modifyAndGet[Runtime] { runtime =>
      if runtime.dead.isDefined then handleDead(runtime, currentTime, height)
      else handleLive(runtime, clickCount, currentTime, width, height)
    }

  def initCanvas[F[_]: Sync](renderer: dom.CanvasRenderingContext2D): F[Unit] =
    Sync[F].delay {
      renderer.font = "50px sans-serif"
      renderer.textAlign = "center"
      renderer.textBaseline = "middle"
    }

  def drawLive[F[_]: Sync](canvas: html.Canvas, playerY: PlayerY, holes: Queue[(Int, Int)]): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- renderer.drawSquare(Point.colored(canvas.width / 2 - 5, playerY - 5, DarkGreen), 10)
      _ <- holes.map { case (holeX, holeY) =>
        for
          _ <- renderer.drawRect(Point.colored(holeX, 0, DarkBlue), 5, holeY - holeSize)
          _ <- renderer.drawRect(Point.colored(holeX, holeY + holeSize, DarkBlue),
            5, canvas.height - holeY - holeSize)
        yield ()
      }.sequence
    yield ()

  def drawDead[F[_]: Sync](canvas: html.Canvas): F[Unit] =
    canvas.context2d.withColor(DarkRed)(_.fillText("Game Over", canvas.width / 2, canvas.height / 2))

  def draw[F[_]: Sync](canvas: html.Canvas, playerY: PlayerY, frame: Frame, obstacles: Queue[Int],
                       dead: Option[Duration]): F[Unit] =
    canvas.clear[F].flatMap { _ =>
      if dead.isDefined then drawDead(canvas)
      else drawLive(canvas, playerY, getHoles(frame, obstacles, canvas.width))
    }

  def program[F[_]: Async](canvas: html.Canvas, rate: FiniteDuration): F[Unit] = Dispatcher[F].use { dispatcher =>
    given Dispatcher[F] = dispatcher
    val renderer = canvas.context2d
    for
      _ <- canvas.resize
      _ <- initCanvas(renderer)
      clickCountR <- Ref.of[F, Int](0)
      _ <- addEventListener(Click)(_ => clickCountR.update(_ + 1))
      startTime <- Clock[F].monotonic
      runtime = Runtime(Random(startTime.toNanos), canvas.height / 2, 0, -50, Queue(), None, startTime)
      _ <- Stream.repeatEval(nextState(clickCountR, canvas.width, canvas.height)).through(state(runtime))
        .evalMap(r => draw(canvas, r.playerY, r.frame, r.obstacles, r.dead))
        .metered(rate).compile.drain
    yield ()
  }

  @JSExportTopLevel("flappyBox")
  def flappyBox(canvas: html.Canvas): Unit = program[IO](canvas, 20.millis).run()