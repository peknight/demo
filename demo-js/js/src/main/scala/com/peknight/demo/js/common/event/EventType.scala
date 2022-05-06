package com.peknight.demo.js.common.event

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

  case object MouseDown extends MouseEventType:
    val eventType = "mousedown"

  case object MouseUp extends MouseEventType:
    val eventType = "mouseup"

  case object Click extends MouseEventType:
    val eventType = "click"

end EventType