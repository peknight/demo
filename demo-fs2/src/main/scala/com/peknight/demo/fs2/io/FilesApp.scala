package com.peknight.demo.fs2.io

import cats.effect.{Async, Concurrent}
import cats.implicits.{catsSyntaxApplicativeError, toFunctorOps}
import fs2.hashing.HashAlgorithm.SHA256
import fs2.io.file.{Files, Path}
import fs2.text
import fs2.hashing.Hashing

object FilesApp:
  def writeDigest[F[_]: Files: Async](path: Path): F[Path] =
    val target = Path(path.toString + ".sha256")
    Files[F].readAll(path)
      .through(Hashing.forSync[F].hash(SHA256))
      .through(_.mapChunks(_.flatMap(_.bytes)))
      .through(text.hex.encode)
      .through(text.utf8.encode)
      .through(Files[F].writeAll(target))
      .compile
      .drain
      .as(target)

  def totalBytes[F[_]: Files: Concurrent](path: Path): F[Long] =
    Files[F].walk(path).evalMap(p => Files[F].size(p).handleError(_ => 0L)).compile.foldMonoid

  def scalaLineCount[F[_]: Files: Concurrent](path: Path): F[Long] =
    Files[F].walk(path).filter(_.extName == ".scala").flatMap { p =>
      Files[F].readAll(p).through(text.utf8.decode).through(text.lines).as(1L)
    }.compile.foldMonoid
