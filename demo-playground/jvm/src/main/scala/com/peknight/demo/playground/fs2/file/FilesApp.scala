package com.peknight.demo.playground.fs2.file

import cats.effect.*
import fs2.io.file.*

object FilesApp extends IOApp.Simple:

  def run = Files[IO].walk(Path(""), WalkOptions.Default.withMaxDepth(1).withFollowLinks(false))
    .evalMap(p => Files[IO].size(p).flatMap(size => IO.println(s"${p.fileName} - ${size} bytes")))
    .compile.drain
