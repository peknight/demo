package com.peknight.demo.zio.overview

import zio.*

import java.io.{FileInputStream, IOException}
import scala.io.BufferedSource

object HandlingResources:

  // Finalizing
  val finalizer: UIO[Unit] = ZIO.succeed(println("Finalizing!"))
  val finalized: IO[String, Unit] = ZIO.fail("Failed!").ensuring(finalizer)

  // Acquire Release
  def openFile(path: String): IO[IOException, BufferedSource] = ???
  def closeFile(source: BufferedSource): UIO[Unit] = ???
  def decodeData(source: BufferedSource): IO[IOException, String] = ???
  def groupData(data: String): IO[IOException, Unit] = ???

  val groupedFileData: IO[IOException, Unit] = ZIO.acquireReleaseWith(openFile("data.json"))(closeFile) { file =>
    for
      data <- decodeData(file)
      grouped <- groupData(data)
    yield grouped
  }

  def openFileInputStream(path: String): IO[IOException, FileInputStream] = ???

  val bytesInFile: IO[Throwable, Int] = ZIO.scoped {
    for
      stream <- ZIO.fromAutoCloseable(openFileInputStream("data.json"))
      data <- ZIO.attemptBlockingIO(stream.readAllBytes())
    yield data.length
  }
