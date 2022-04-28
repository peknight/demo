package com.peknight.demo.js.stream

import org.scalajs.dom

sealed trait EventType[T <: dom.Event]:
  def eventType: String
end EventType

object EventType:

  sealed trait KeyboardEventType extends EventType[dom.KeyboardEvent]
  sealed trait MouseEventType extends EventType[dom.MouseEvent]

  case object KeyPress extends KeyboardEventType:
    val eventType = "keypress"

  case object KeyDown extends KeyboardEventType:
    val eventType = "keydown"

  case object KeyUp extends KeyboardEventType:
    val eventType = "keyup"

  case object MouseMove extends MouseEventType:
    val eventType = "mousemove"

end EventType