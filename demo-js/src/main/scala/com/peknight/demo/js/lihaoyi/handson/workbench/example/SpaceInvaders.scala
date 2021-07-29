package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders {

  case class Runtime(player: Point, enemies: Seq[Point], bullets: Seq[Point], keysDown: Set[Int],
                     width: Int, height: Int, count: Int, wave: Int)

  class Event(var keysDown: Set[Int], var bullet: Boolean)

  def screenSize(window: dom.Window): (Int, Int) = (window.innerWidth.toInt, window.innerHeight.toInt)

  def updateScreenSize(window: dom.Window): State[Runtime, Unit] = State.modify(runtime => {
    val (width, height) = screenSize(window)
    runtime.copy(width = width, height = height)
  })

  def addCount: State[Runtime, Unit] = State.modify(runtime => runtime.copy(count = runtime.count + 1))

  def addBullets(add: Boolean): State[Runtime, Unit] = State.modify { runtime =>
    if (add) {
      runtime.copy(bullets = runtime.bullets ++ Seq(runtime.player))
    } else {
      runtime
    }
  }

  def updateKeysDown(keysDown: Set[Int]): State[Runtime, Unit] = State.modify(_.copy(keysDown = keysDown))

  def moveBullets: State[Runtime, Unit] = State.modify(runtime => runtime.copy(bullets =
    runtime.bullets.map(p => Point(p.x, p.y - 5))
  ))

  def checkEnemyClear: State[Runtime, Unit] = State.modify(runtime =>
    if (runtime.enemies.isEmpty) {
      val enemies = for {
        x <- (0 until runtime.width by 50)
        y <- 0 until runtime.wave
      } yield Point(x, 50 + y * 50)
      runtime.copy(enemies = enemies, wave = runtime.wave + 1)
    } else {
      runtime
    }
  )

  def checkEnemyDestroy: State[Runtime, Unit] = State.modify(runtime => runtime.copy(enemies =
    runtime.enemies.filter(enemy => !runtime.bullets.exists(bullet => (enemy - bullet).length < 5))
  ))

  def moveEnemy: State[Runtime, Unit] = State.modify(runtime => runtime.copy(enemies =
    runtime.enemies.map(enemy => {
      val i = runtime.count % 200
      if (i < 50) enemy.copy(x = enemy.x - 0.2)
      else if (i < 100) enemy.copy(y = enemy.y + 0.2)
      else if (i < 150) enemy.copy(x = enemy.x + 0.2)
      else enemy.copy(y = enemy.y + 0.2)
    })
  ))

  val LEFT = (37, Point(-2, 0))
  val UP = (38, Point(0, -2))
  val RIGHT = (39, Point(2, 0))
  val DOWN = (40, Point(0, 2))

  def movePlayer(direction: (Int, Point)): State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.keysDown(direction._1)) {
      runtime.copy(player = runtime.player + direction._2)
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

    ctx.fillStyle = "white"
    ctx.fillRect(runtime.player.x - 5, runtime.player.y - 5, 10, 10)

    ctx.fillStyle = "yellow"
    for (enemy <- runtime.enemies) {
      ctx.fillRect(enemy.x - 5, enemy.y - 5, 10, 10)
    }

    ctx.fillStyle = "red"
    for (bullet <- runtime.bullets) {
      ctx.fillRect(bullet.x - 2, bullet.y - 2, 4, 4)
    }
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit = {

    dom.console.log("main")

    val event = new Event(Set(), false)
    dom.document.onkeypress = { (e: dom.KeyboardEvent) => if (e.keyCode == 32) event.bullet = true }
    dom.document.onkeydown = { (e: dom.KeyboardEvent) => event.keysDown = event.keysDown + e.keyCode }
    dom.document.onkeyup = { (e: dom.KeyboardEvent) => event.keysDown = event.keysDown - e.keyCode }

    val (width, height) = screenSize(dom.window)

    run(dom.window, canvas)
      .run(Runtime(Point(width / 2, height / 2), Seq(), Seq(), event.keysDown, width, height, 0, 1))
      .value

    def run(window: dom.Window, canvas: html.Canvas): State[Runtime, Unit] = for {
      _ <- updateScreenSize(window)
      _ <- addCount
      _ <- addBullets(event.bullet)
      _ = event.bullet = false
      _ <- updateKeysDown(event.keysDown)
      _ <- moveBullets
      _ <- checkEnemyClear
      _ <- checkEnemyDestroy
      _ <- moveEnemy
      _ <- movePlayer(LEFT)
      _ <- movePlayer(UP)
      _ <- movePlayer(RIGHT)
      _ <- movePlayer(DOWN)
      _ <- draw(canvas)
      runtime <- State.get[Runtime]
      _ = window.setTimeout(() => run(window, canvas).run(runtime).value, 20)
    } yield ()
  }
}
