package com.peknight.demo.js.common.event

import cats.effect.Sync
import cats.effect.std.Dispatcher
import org.scalajs.dom

object EventListenerOps:

  def addEventListener[F[_] : Sync, T <: dom.Event](eventType: EventType[T], target: dom.EventTarget = dom.document)
                                                   (callback: PartialFunction[T, F[Unit]])
                                                   (using dispatcher: Dispatcher[F]): F[Unit] =
    Sync[F].delay(target.addEventListener[T](eventType.eventType,
      t => if callback.isDefinedAt(t) then dispatcher.unsafeToPromise(callback(t)) else Sync[F].unit
    ))
