package com.peknight.demo.fs2.timeseries

import cats.Functor
import cats.effect.kernel.Temporal
import cats.effect.{Clock, IO, IOApp, Ref}
import fs2.timeseries.{TimeSeries, TimeStamped}
import fs2.{Chunk, Scan, Stream}
import scodec.bits.ByteVector

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

object TimeSeriesApp extends IOApp.Simple:

  def withBitrateV1[F[_]](input: Stream[F, TimeStamped[ByteVector]]): Stream[F, TimeStamped[Either[Long, ByteVector]]] =
    TimeStamped.withPerSecondRate[ByteVector, Long](_.size * 8).toPipe(input)

  def withReceivedBitrateV1[F[_]: Functor: Clock](input: Stream[F, Byte]): Stream[F, TimeStamped[Either[Long, ByteVector]]] =
    input.chunks.evalMap(c => TimeStamped.now(c.toByteVector)).through(withBitrateV1)

  def withAverageBitrate[F[_]: Functor: Clock](input: Stream[F, Byte]): Stream[F, TimeStamped[Either[Long, ByteVector]]] =
    withReceivedBitrateV1(input).mapAccumulate(Queue.empty[Long]) {
      case (q, tsv @ TimeStamped(_, Right(_))) => (q, tsv)
      case (q, TimeStamped(t, Left(sample))) =>
        //noinspection DuplicatedCode
        val q2 = (sample +: q).take(10)
        val average = q2.sum / q2.size
        (q2, TimeStamped(t, Left(average)))
    }.map(_._2)

  def measureAverageBitrateV1[F[_]: Functor: Clock](store: Ref[F, Long], input: Stream[F, Byte]): Stream[F, Byte] =
    withAverageBitrate(input).flatMap {
      case TimeStamped(_, Left(bitrate)) => Stream.exec(store.set(bitrate))
      case TimeStamped(_, Right(bytes)) => Stream.chunk(Chunk.byteVector(bytes))
    }

  def withBitrateV2[F[_]](input: Stream[F, TimeStamped[Option[ByteVector]]])
  : Stream[F, TimeStamped[Either[Long, Option[ByteVector]]]] =
    TimeStamped.withPerSecondRate[Option[ByteVector], Long](_.map(_.size).getOrElse(0L) * 8).toPipe(input)

  def withReceivedBitrateV2[F[_]: Temporal](input: Stream[F, Byte])
  : Stream[F, TimeStamped[Either[Long, Option[ByteVector]]]] =
    TimeSeries.timePulled(input.chunks.map(_.toByteVector), 1.second, 1.second).through(withBitrateV2)

  def bitrate =
    TimeStamped.withPerSecondRate[Option[ByteVector], Long](_.map(_.size).getOrElse(0L) * 8)

  def averageBitrate = bitrate.andThen(Scan.stateful1(Queue.empty[Long]) {
    case (q, tsv @ TimeStamped(_, Right(_))) => (q, tsv)
    case (q, TimeStamped(t, Left(sample))) =>
      //noinspection DuplicatedCode
      val q2 = (sample +: q).take(10)
      val average = q2.sum / q2.size
      (q2, TimeStamped(t, Left(average)))
  })

  def measureAverageBitrate[F[_]: Temporal](store: Ref[F, Long], input: Stream[F, Byte]): Stream[F, Byte] =
    TimeSeries.timePulled(input.chunks.map(_.toByteVector), 1.second, 1.second)
      .through(averageBitrate.toPipe)
      .flatMap {
        case TimeStamped(_, Left(bitrate)) => Stream.exec(store.set(bitrate))
        case TimeStamped(_, Right(Some(bytes))) => Stream.chunk(Chunk.byteVector(bytes))
        case TimeStamped(_, Right(None)) => Stream.empty
      }

  val inputStream = Stream.constant(1.asInstanceOf[Byte]).metered[IO](1.millis)

  val duration = 5.seconds

  val run =
    for
      store <- Ref.of[IO, Long](0)
      _ <- measureAverageBitrate[IO](store, inputStream.interruptAfter(duration)).interruptAfter(duration).compile.drain
      bitrate <- store.get
      _ <- IO.println(s"bitrate: $bitrate")
    yield ()
