package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.syntax.traverse._
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots {

  case class Point(x: Double, y: Double) {
    def +(p: Point) = Point(x + p.x, y + p.y)
    def -(p: Point) = Point(x - p.x, y - p.y)
    def *(d: Double) = Point(x * d, y * d)
    def /(d: Double) = Point(x / d, y / d)
    def length = Math.sqrt(x * x + y * y)
  }

  case class Enemy(pos: Point, vel: Point)

  case class Runtime(rng: RNG,
                     player: Point,
                     enemies: Seq[Enemy],
                     width: Int,
                     height: Int,
                     startTime: Double,
                     death: Option[(String, Int)]
                    )

  class Mouse(var point: Point)

  def screenSize(window: dom.Window): (Int, Int) = (window.innerWidth.toInt, window.innerHeight.toInt)

  def reset(window: dom.Window, mouse: Point): State[Runtime, Unit] = State.modify { runtime =>
    val (width, height) = screenSize(window)
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
    runtime.copy(player = mouse, width = width, height = height, startTime = startTime, death = death)
  }

  def randNonNegativeLessThan(n: Int): State[Runtime, Int] = State { runtime =>
    val (i, rng) = runtime.rng.nonNegativeLessThan(n)
    (runtime.copy(rng = rng), i)
  }

  def randSpeed: State[Runtime, Int] = randNonNegativeLessThan(5).map(_ - 3)

  def filterEnemies: State[Runtime, Unit] = State.modify { runtime =>
    val enemies = runtime.enemies.filter(e =>
      e.pos.x >= 0 && e.pos.x <= runtime.width && e.pos.y >= 0 && e.pos.y <= runtime.height
    )
    runtime.copy(enemies = enemies)
  }

  def createEnemy: State[Runtime, Enemy] = for {
    runtime <- State.get[Runtime]
    x <- randNonNegativeLessThan(runtime.width)
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
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    canvas.width = runtime.width
    canvas.height = runtime.height
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, runtime.width, runtime.height)
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
        ctx.fillText(s"${deltaT(runtime.startTime)} seconds", runtime.width / 2 - 100, runtime.height / 5)
      case Some((msg, time)) =>
        ctx.fillStyle = "white"
        ctx.fillText(msg, runtime.width / 2 - 100, runtime.height / 2)
    }
  }

  def deltaT(startTime: Double) = ((js.Date.now() - startTime) / 1000).toInt

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = {
    dom.console.log("main")

    val window = dom.window
    val (width, height) = screenSize(window)

    val mouse: Mouse = new Mouse(Point(width / 2, height / 2))
    dom.document.onmousemove = { (e: dom.MouseEvent) => mouse.point = Point(e.clientX, e.clientY) }

    val runtime = Runtime(RNG(0), mouse.point, Seq.empty[Enemy], width, height, js.Date.now(), None)

    run(dom.window, canvas).run(runtime).value

    def run(window: dom.Window, canvas: html.Canvas): State[Runtime, Unit] = for {
      _ <- reset(window, mouse.point)
      _ <- filterEnemies
      _ <- fillEnemies
      _ <- moveEnemies
      _ <- checkDeath
      _ <- draw(canvas)
      runtime <- State.get[Runtime]
      _ = window.setTimeout(() => run(window, canvas).run(runtime).value, 20)
    } yield ()
  }
}
