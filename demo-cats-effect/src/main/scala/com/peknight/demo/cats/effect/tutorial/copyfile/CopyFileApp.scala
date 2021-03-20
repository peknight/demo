package com.peknight.demo.cats.effect.tutorial.copyfile

import cats.effect.{IO, Resource}
import cats.syntax.apply._

import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}

object CopyFileApp extends App {
  def copy(origin: File, destination: File): IO[Long] = ???

  def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO(new FileInputStream(f))
    } { inStream =>
      IO(inStream.close()).handleErrorWith(_ => IO.unit)
    }

  def outputStream(f: File): Resource[IO, FileOutputStream] =
    Resource.make {
      IO(new FileOutputStream(f))
    } { outStream =>
      IO(outStream.close()).handleErrorWith(_ => IO.unit)
    }

  def inputOutputStreams(in: File, out: File): Resource[IO, (InputStream, OutputStream)] =
    (inputStream(in), outputStream(out)).mapN((_, _))
}
