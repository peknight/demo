package com.peknight.demo.catseffect.standard

import cats.effect.{IO, Resource}

import java.io.File

object ResourceApp:

  def openFile(name: String): IO[File] = ???
  def close(file: File): IO[Unit] = ???
  def read(file: File): IO[Array[Byte]] = ???
  def write(file: File, bytes: Array[Byte]): IO[Unit] = ???

  def file(name: String): Resource[IO, File] = Resource.make(openFile(name))(file => close(file))

  val concat: IO[Unit] =
    (
      for
        in1 <- file("file1")
        in2 <- file("file2")
        out <- file("file3")
      yield (in1, in2, out)
    ).use { case (file1, file2, file3) =>
      for
        bytes1 <- read(file1)
        bytes2 <- read(file2)
        _ <- write(file3, bytes1 ++ bytes2)
      yield ()
    }
