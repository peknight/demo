package com.peknight.demo.js.lihaoyi.handson.canvas

import cats.data.State
import com.peknight.demo.js.domain.{CanvasSize, RNG}
import com.peknight.demo.js.util.DomUtil
import org.scalajs.dom
import org.scalajs.dom.html

import scala.collection.immutable.Queue
import scala.scalajs.js.annotation.JSExportTopLevel

object FlappyBox {

  /* variables */
  val obstacleGap = 200 // Gap between the approaching obstacles
  val holeSizeRatio = 50.0 / 255 // Size of the hole in each obstacle you must go through
  val gravity = 0.1 // Y accelaration of the player

  case class Runtime(
                      playerY: Double,  // Y position of the player; x is fixed
                      playerV: Double, // Y velocity of the player
                      // Whether the player is dead or not;
                      // None means alive, Some(>0) is number of frames before respawning
                      dead: Option[Int],
                      frame: Int, // What frame this is; used to keep track of where the obstacles should be positioned
                      // List of each obstacle, storing only the Y position of the hole.
                      // The X position of the obstacle is calculated by its position in the
                      // queue and in the current frame.
                      obstacles: Queue[Int],
                      canvasSize: CanvasSize,
                      rng: RNG
                    )

  class ClickCount(var count: Int) {
    def getAndReset: Int = {
      val count = this.count
      this.count = 0
      count
    }
  }

  def holeSize(height: Int): Int = (holeSizeRatio * height).toInt

  def updateCanvasSize(window: dom.Window): State[Runtime, Unit] =
    State.modify(_.copy(canvasSize = DomUtil.canvasSizeByWindow(window)))

  def init(canvas: html.Canvas): State[Runtime, Unit] = State.inspect { runtime =>
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    canvas.width = runtime.canvasSize.width
    canvas.height = runtime.canvasSize.height
    renderer.font = "50px sans-serif"
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"
  }

  def randNonNegativeLessThan(n: Int): State[Runtime, Int] = State { runtime =>
    val (i, rng) = runtime.rng.nonNegativeLessThan(n)
    (runtime.copy(rng = rng), i)
  }

  def createNewObstacles: State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.frame >= 0 && runtime.frame % obstacleGap == 0) {
      val height = runtime.canvasSize.height
      val (rand, rng) = runtime.rng.nonNegativeLessThan(height - 2 * holeSize(height))
      runtime.copy(obstacles = runtime.obstacles.enqueue(rand + holeSize(height)), rng = rng)
    } else {
      runtime
    }
  }

  def killOldObstacles: State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.obstacles.length > 7) {
      val (_, obstacles) = runtime.obstacles.dequeue
      runtime.copy(frame = runtime.frame - obstacleGap, obstacles = obstacles)
    } else {
      runtime
    }
  }

  def getHoles: State[Runtime, Queue[(Int, Int)]] = State.inspect { runtime =>
    runtime.obstacles.zipWithIndex.map { case (holeY, i) =>
      val holeX = i * obstacleGap - runtime.frame + runtime.canvasSize.width
      (holeX, holeY)
    }
  }

  def checkDead(holes: Queue[(Int, Int)]): State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.playerY < 0 || runtime.playerY > runtime.canvasSize.height) {
      runtime.copy(dead = Some(50))
    } else {
      val hit = holes.find { case (holeX, holeY) =>
        math.abs(holeX - runtime.canvasSize.width / 2) < 5 &&
          math.abs(holeY - runtime.playerY) > holeSize(runtime.canvasSize.height)
      }
      if (hit.isDefined) runtime.copy(dead = Some(50)) else runtime
    }
  }

  def drawObstacles(canvas: html.Canvas, holes: Queue[(Int, Int)]): State[Runtime, Unit] = State.inspect { runtime =>
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    renderer.fillStyle = "darkblue"
    for((holeX, holeY) <- holes) {
      val size = holeSize(runtime.canvasSize.height)
      renderer.fillRect(holeX, 0, 5, holeY - size)
      renderer.fillRect(holeX, holeY + size, 5, runtime.canvasSize.height - holeY - size)
    }
  }

  def drawPlayer(canvas: html.Canvas): State[Runtime, Unit] = State.inspect { runtime =>
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    renderer.fillStyle = "darkgreen"
    renderer.fillRect(runtime.canvasSize.width / 2 - 5, runtime.playerY - 5, 10, 10)
  }

  def runLive(canvas: html.Canvas): State[Runtime, Unit] = for {
    _ <- State.modify[Runtime](runtime => runtime.copy(frame = runtime.frame + 2))
    _ <- createNewObstacles
    _ <- killOldObstacles
    _ <- State.modify[Runtime](runtime =>
      runtime.copy(playerY = runtime.playerY + runtime.playerV, playerV = runtime.playerV + gravity))
    holes <- getHoles
    _ <- checkDead(holes)
    _ <- drawObstacles(canvas, holes)
    _ <- drawPlayer(canvas)
  } yield ()

  def resetOnDead: State[Runtime, Unit] = State.modify { runtime =>
    runtime.copy(
      playerY = runtime.canvasSize.height / 2,
      playerV = 0,
      dead = runtime.dead.flatMap { d => if (d <= 1) None else Some(d - 1) },
      frame = -50,
      obstacles = Queue()
    )
  }

  def drawGameOver(canvas: html.Canvas): State[Runtime, Unit] = State.inspect { runtime =>
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    renderer.fillStyle = "darkred"
    renderer.fillText("Game Over", runtime.canvasSize.width / 2, runtime.canvasSize.height / 2)
  }

  def runDead(canvas: html.Canvas): State[Runtime, Unit] = for {
    _ <- resetOnDead
    _ <- drawGameOver(canvas)
  } yield ()

  def clearCanvas(canvas: html.Canvas): State[Runtime, Unit] = State.inspect { runtime =>
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    renderer.clearRect(0, 0, runtime.canvasSize.width, runtime.canvasSize.height)
  }

  def updatePlayerV(clickCount: Int): State[Runtime, Unit] = State.modify { runtime =>
    runtime.copy(playerV = if (clickCount > 0) -3.2 else runtime.playerV)
  }

  def run(canvas: html.Canvas, clickCount: Int): State[Runtime, Unit] = for {
    _ <- updatePlayerV(clickCount)
    _ <- clearCanvas(canvas)
    runtime <- State.get[Runtime]
    _ <- if (runtime.dead.isDefined) runDead(canvas) else runLive(canvas)
  } yield ()

  @JSExportTopLevel("flappyBox")
  def flappyBox(canvas: html.Canvas): Unit = {
    val size @ CanvasSize(width, height) = DomUtil.canvasSizeByParentElement(canvas)
    println(width, height)
    val runtime = updateCanvasSize(dom.window).flatMap(_ => init(canvas))
      .run(Runtime(height / 2.0, 0.0, None, -50, Queue(), size, RNG(0))).value._1

    val clickCount = new ClickCount(0)
    canvas.onclick = _ => clickCount.count = clickCount.count + 1
    DomUtil.interval(dom.window, run(canvas, clickCount.getAndReset), runtime, 20)
  }
}
