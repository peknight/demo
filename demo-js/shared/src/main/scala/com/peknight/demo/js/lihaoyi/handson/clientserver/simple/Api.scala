package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

trait Api[F[_]]:
  def list(path: FilePath): F[Seq[FileData]]

