package com.peknight.demo.js.stream

import cats.effect.Async
import cats.effect.std.Dispatcher
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import fs2.concurrent.Topic
import org.scalajs.dom

object EventTopic:

  def eventTopic[F[_]: Async, E](dispatcher: Dispatcher[F])(update: (E => Unit) => Unit)
  : F[Topic[F, E]] =
    for
      topic <- Topic[F, E]
      _ <- Async[F].delay { update(e => dispatcher.unsafeToPromise(topic.publish1(e))) }
    yield topic

  def eventTopic[F[_]: Async, T <: dom.Event](eventType: EventType[T], target: dom.EventTarget = dom.document)
                                             (dispatcher: Dispatcher[F]): F[Topic[F, T]] =
    eventTopic(dispatcher)(cb => target.addEventListener[T](eventType.eventType, cb))
