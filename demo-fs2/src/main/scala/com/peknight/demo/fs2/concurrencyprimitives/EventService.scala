package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.{Clock, Temporal}
import cats.effect.std.Console
import cats.implicits.toFunctorOps
import com.peknight.demo.fs2.concurrencyprimitives.Event.{Quit, Text}
import fs2.concurrent.{SignallingRef, Topic}
import fs2.{INothing, Pipe, Stream}

import scala.concurrent.duration.DurationInt

class EventService[F[_]](eventsTopic: Topic[F, Event], interrupter: SignallingRef[F, Boolean])
                        (using F: Temporal[F], console: Console[F]):
  // Publishing 15 text events, then single Quit event, and still publishing text events
  def startPublisher: Stream[F, Unit] =
    val textEvents = Stream.awakeEvery[F](1.second)
      .zipRight(Stream.repeatEval(Clock[F].realTime.map(t => Text(t.toString))))
    val quitEvent = Stream.eval(eventsTopic.publish1(Quit).as(Quit))
    (textEvents.take(15) ++ quitEvent ++ textEvents).through(eventsTopic.publish).interruptWhen(interrupter)

  def startSubscribers: Stream[F, Unit] =
    def processEvent(subscriberNumber: Int): Pipe[F, Event, INothing] = _.foreach {
      case e @ Text(_) => console.println(s"Subscriber #$subscriberNumber processing event: $e")
      case Quit => interrupter.set(true)
    }
    val events: Stream[F, Event] = eventsTopic.subscribe(10)
    Stream(
      events.through(processEvent(1)),
      events.delayBy(5.second).through(processEvent(2)),
      events.delayBy(10.second).through(processEvent(3)),
    ).parJoin(3)
