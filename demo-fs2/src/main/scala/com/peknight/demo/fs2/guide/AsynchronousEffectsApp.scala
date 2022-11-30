package com.peknight.demo.fs2.guide

import cats.effect.std.{Dispatcher, Queue}
import cats.effect.{Async, IO, IOApp}
import fs2.Stream

object AsynchronousEffectsApp extends IOApp.Simple:

  /*
   * Asynchronous effects (callbacks invoked once)
   */

  trait Connection:
    def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit
    def readBytesE(onComplete: Either[Throwable, Array[Byte]] => Unit): Unit =
      readBytes(bs => onComplete(Right(bs)), e => onComplete(Left(e)))
    override def toString = "<connection>"

  val c = new Connection:
    def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit =
      Thread.sleep(200)
      onSuccess(Array(0, 1, 2))

  // translate from a callback-based API to a straightforward monadic version.
  val bytes = IO.async_[Array[Byte]] { cb => c.readBytesE(cb) }

  /*
   * Asynchronous effects (callbacks invoked multiple times)
   */

  type Row = List[String]
  type RowOrError = Either[Throwable, Row]

  trait CSVHandle:
    def withRows(cb: RowOrError => Unit): Unit

  def rows[F[_]](h: CSVHandle)(using F: Async[F]): Stream[F, Row] =
    for
      dispatcher <- Stream.resource(Dispatcher.sequential[F])
      q <- Stream.eval(Queue.unbounded[F, Option[RowOrError]])
      _ <- Stream.eval { F.delay {
        def enqueue(v: Option[RowOrError]): Unit = dispatcher.unsafeRunAndForget(q.offer(v))

        // Fill the data - withRows blocks while reading the file, asynchronously:
        // invoking the callback we pass to it on every row
        h.withRows(e => enqueue(Some(e)))
        // Upon returning from withRows, signal that our stream has ended.
        enqueue(None)
      }}
      row <- Stream.fromQueueNoneTerminated(q).rethrow
    yield row

  val csvHandle = new CSVHandle:
    def withRows(cb: RowOrError => Unit): Unit =
      Thread.sleep(200)
      cb(Right[Throwable, Row](List("a", "b", "c")))
      Thread.sleep(200)
      cb(Right[Throwable, Row](List("d")))
      Thread.sleep(200)
      cb(Right[Throwable, Row](List("e", "f", "g")))


  val run =
    for
      _ <- Stream.eval(bytes).map(_.toList).compile.toVector.flatMap(IO.println)
      _ <- rows[IO](csvHandle).compile.toVector.flatMap(IO.println)
    yield ()
