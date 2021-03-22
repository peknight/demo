package com.peknight.demo.cats.effect.tutorial.copyfile

import cats.effect.concurrent.Semaphore
import cats.effect.{Concurrent, ExitCode, IO, IOApp, Resource, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._

import java.io._

object CopyFilePolymorphic extends IOApp {

  def transmit[F[_]: Sync](origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): F[Long] =
    for {
      amount <- Sync[F].delay(origin.read(buffer, 0, buffer.size))
      count <- if (amount > -1) Sync[F].delay(destination.write(buffer, 0, amount)) >> transmit(origin, destination, buffer, acc + amount) else Sync[F].pure(acc)
    } yield count

  def transfer[F[_]: Sync](origin: InputStream, destination: OutputStream): F[Long] =
    for {
      buffer <- Sync[F].delay(new Array[Byte](1024 * 10))
      total <- transmit(origin, destination, buffer, 0L)
    } yield total

  def inputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].delay(new FileInputStream(f))
    } { inStream =>
      guard.withPermit {
        // handleErrorWith 需要import cats.syntax.applicativeError._
        Sync[F].delay(inStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }

  def outputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileOutputStream] = {
    Resource.make {
      Sync[F].delay(new FileOutputStream(f))
    } { outStream =>
      guard.withPermit {
        Sync[F].delay(outStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }
  }

  def inputOutputStreams[F[_]: Sync](in: File, out: File, guard: Semaphore[F]): Resource[F, (InputStream, OutputStream)] = {
    for {
      inStream <- inputStream(in, guard)
      outStream <- outputStream(out, guard)
    } yield (inStream, outStream)
  }

  def copy[F[_]: Concurrent](origin: File, destination: File): F[Long] = {
    for {
      guard <- Semaphore[F](1)
      count <- inputOutputStreams(origin, destination, guard).use {
        case (in, out) => guard.withPermit(transfer(in, out))
      }
    } yield count
  }

  override def run(args: List[String]): IO[ExitCode] = {
//    for {
//      _ <- if (args.length < 2) IO.raiseError(new IllegalArgumentException("Need origin and destination files")) else IO.unit
//      orig = new File(args(0))
//      dest = new File(args(1))
//      count <- copy[IO](orig, dest)
//      _ <- IO(println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}"))
//    } yield ExitCode.Success
    for {
      _ <- if (args.length < 2) IO.raiseError(new IllegalArgumentException("Need origin and destination files")) else IO.unit
      orig = new File(args(0))
      dest = new File(args(1))
      _ <- if (orig == dest) IO.raiseError(new IllegalArgumentException("origin and destination can't be same")) else IO.unit
      continue <- if (dest.exists()) (IO(println(s"destination file ${dest.getPath()} already exists, continue?")) >> IO(scala.io.StdIn.readBoolean())) else IO(true)
      count <- if (continue) copy[IO](orig, dest) else IO(0L)
      _ <- IO(println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}"))
    } yield ExitCode.Success
  }
}
