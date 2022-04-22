package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

trait Api {
  def list(path: String): Seq[FileData]
}
