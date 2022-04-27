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

  def onKeyPressTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeypress = cb)

  def onKeyDownTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeydown = cb)

  def onKeyUpTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.KeyboardEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onkeyup = cb)

  def onMouseMoveTopic[F[_]: Async](dispatcher: Dispatcher[F]): F[Topic[F, dom.MouseEvent]] =
    eventTopic(dispatcher)(cb => dom.document.onmousemove = cb)

