package com.peknight.demo.zio.overview

import zio.*

import java.io.{FileNotFoundException, IOException}

object HandlingErrorsApp:
  // Either
  val zEither: UIO[Either[String, Nothing]] = ZIO.fail("Uh oh!").either

  // Catching All Errors
  def openFile(path: String): IO[IOException, Array[Byte]] = ???
  val z: IO[IOException, Array[Byte]] = openFile("primary.json").catchAll { error =>
    for
      _ <- ZIO.logErrorCause("Could not open primary file", Cause.fail(error))
      file <- openFile("backup.json")
    yield file
  }


  // Catching Some Errors
  val data: IO[IOException, Array[Byte]] = openFile("primary.data").catchSome {
    case _: FileNotFoundException => openFile("backup.data")
  }

  // Fallback
  val primaryOrBackupData: IO[IOException, Array[Byte]] =
    openFile("primary.data").orElse(openFile("backup.data"))

  // Folding
  lazy val DefaultData: Array[Byte] = Array(0, 0)
  val primaryOrDefaultData: UIO[Array[Byte]] = openFile("primary.data").fold(_ => DefaultData, data => data)

  val primaryOrSecondaryData: IO[IOException, Array[Byte]] =
    openFile("primary.data").foldZIO(_ => openFile("secondary.data"), data => ZIO.succeed(data))


  trait Content
  object Content:
    case class NoContent(error: IOException) extends Content
  end Content
  type ReadUrlsResult = String
  def readUrls(path: String): IO[IOException, ReadUrlsResult] = ???
  def fetchContent(result: ReadUrlsResult): UIO[Content] = ???
  val urls: UIO[Content] = readUrls("urls.json").foldZIO(
    error => ZIO.succeed(Content.NoContent(error)),
    success => fetchContent(success)
  )

  // Retrying
  val retriedOpenFile: IO[IOException, Array[Byte]] = openFile("primary.data").retry(Schedule.recurs(5))
  val retryOpenFile: IO[IOException, Array[Byte]] =
    openFile("primary.data").retryOrElse(Schedule.recurs(5), (_, _) => ZIO.succeed(DefaultData))

