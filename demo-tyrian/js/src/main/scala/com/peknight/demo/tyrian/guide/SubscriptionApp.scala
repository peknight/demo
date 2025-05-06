package com.peknight.demo.tyrian.guide

import cats.effect.IO
import org.scalajs.dom.{MouseEvent, document}
import tyrian.*

import scala.concurrent.duration.*

object SubscriptionApp:
  type Model = Int

  enum Msg:
    case MouseMove(x: Double, y: Double)
    case CurrentSeconds(seconds: Double)
  end Msg

  val mousePosition: Sub[IO, Msg] =
    Sub.fromEvent("mousemove", document) {
      case e: MouseEvent => Option(Msg.MouseMove(e.pageX, e.pageY))
    }

  val tick = Sub.every[IO](1.second, "tick").map(date => Msg.CurrentSeconds(date.getSeconds()))

  def subscriptions(model: Model): Sub[IO, Msg] =
    // Sub.Batch[IO, Msg](mousePosition, tick)
    mousePosition |+| tick
end SubscriptionApp
