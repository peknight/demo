package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.syntax.traverse._
import com.peknight.demo.js.domain.{CanvasSize, Point, RNG}
import com.peknight.demo.js.util.DomUtil
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots {

  case class Enemy(pos: Point, vel: Point)

  case class Runtime(rng: RNG,
                     player: Point,
                     enemies: Seq[Enemy],
                     canvasSize: CanvasSize,
                     startTime: Double,
                     death: Option[(String, Int)]
                    )

  class Mouse(var point: Point)

  def reset(window: dom.Window, mouse: Point): State[Runtime, Unit] = State.modify { runtime =>
    val canvasSize = DomUtil.canvasSizeByWindow(window)
    val (death, startTime) = runtime.death match {
      case Some((msg, time)) => {
        if (time - 1 == 0) {
          (None, js.Date.now())
        } else {
          (Option((msg, time - 1)), runtime.startTime)
        }
      }
      case None => (None, runtime.startTime)
    }
    runtime.copy(player = mouse, canvasSize = canvasSize, startTime = startTime, death = death)
  }

  def randNonNegativeLessThan(n: Int): State[Runtime, Int] = State { runtime =>
    val (i, rng) = runtime.rng.nonNegativeLessThan(n)
    (runtime.copy(rng = rng), i)
  }

  def randSpeed: State[Runtime, Int] = randNonNegativeLessThan(5).map(_ - 3)

  def filterEnemies: State[Runtime, Unit] = State.modify { runtime =>
    val enemies = runtime.enemies.filter(e =>
      e.pos.x >= 0 && e.pos.x <= runtime.canvasSize.width && e.pos.y >= 0 && e.pos.y <= runtime.canvasSize.height
    )
    runtime.copy(enemies = enemies)
  }

  def createEnemy: State[Runtime, Enemy] = for {
    runtime <- State.get[Runtime]
    x <- randNonNegativeLessThan(runtime.canvasSize.width)
    xSpeed <- randSpeed
    ySpeed <- randSpeed
  } yield Enemy(Point(x, 0), Point(xSpeed, ySpeed))

  def createEnemies(size: Int): State[Runtime, Seq[Enemy]] = Seq.fill(size)(createEnemy).sequence

  def fillEnemies: State[Runtime, Unit] = for {
    runtime <- State.get[Runtime]
    enemies <- createEnemies(20 - runtime.enemies.size)
    _ <- State.modify[Runtime](r => r.copy(enemies = r.enemies ++ enemies))
  } yield ()

  def moveEnemies: State[Runtime, Unit] = for {
    runtime <- State.get[Runtime]
    enemies = runtime.enemies.map(enemy => {
      val delta = runtime.player - enemy.pos
      Enemy(enemy.pos + enemy.vel, enemy.vel + delta / delta.length / 100)
    })
    _ <- State.modify[Runtime](r => r.copy(enemies = enemies))
  } yield ()

  def checkDeath: State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.enemies.exists(e => (e.pos - runtime.player).length < 20)) {
      val death = Some((s"You lasted ${deltaT(runtime.startTime)} seconds", 100))
      val enemies = runtime.enemies.filter(e => (e.pos - runtime.player).length > 20)
      runtime.copy(enemies = enemies, death = death)
    } else {
      runtime
    }
  }

  def draw(canvas: html.Canvas): State[Runtime, Unit] = State.inspect { runtime =>
    val CanvasSize(width, height) = runtime.canvasSize
    val ctx = DomUtil.canvasRenderingContext2D(canvas)
    canvas.width = width
    canvas.height = height
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, width, height)
    runtime.death match {
      case None =>
        ctx.fillStyle = "white"
        ctx.fillRect(runtime.player.x - 10, runtime.player.y - 10, 20, 20)
        ctx.fillText("player", runtime.player.x - 15, runtime.player.y - 30)

        ctx.fillStyle = "red"
        for (enemy <- runtime.enemies) {
          ctx.fillRect(enemy.pos.x - 10, enemy.pos.y - 10, 20, 20)
        }
        ctx.fillStyle = "white"
        ctx.fillText(s"${deltaT(runtime.startTime)} seconds", width / 2 - 100, height / 5)
      case Some((msg, time)) =>
        ctx.fillStyle = "white"
        ctx.fillText(msg, width / 2 - 100, height / 2)
    }
  }

  def deltaT(startTime: Double) = ((js.Date.now() - startTime) / 1000).toInt

  def run(window: dom.Window, canvas: html.Canvas, mouse: Mouse): State[Runtime, Unit] = for {
    _ <- reset(window, mouse.point)
    _ <- filterEnemies
    _ <- fillEnemies
    _ <- moveEnemies
    _ <- checkDeath
    _ <- draw(canvas)
  } yield ()

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = {
    dom.console.log("main")

    val window = dom.window
    val canvasSize = DomUtil.canvasSizeByWindow(window)

    val mouse: Mouse = new Mouse(Point(canvasSize.width / 2, canvasSize.height / 2))
    dom.document.onmousemove = { (e: dom.MouseEvent) => mouse.point = Point(e.clientX, e.clientY) }

    val runtime = Runtime(RNG(0), mouse.point, Seq.empty[Enemy], canvasSize, js.Date.now(), None)
    DomUtil.interval(dom.window, run(window, canvas, mouse), runtime, 20)
  }
}
