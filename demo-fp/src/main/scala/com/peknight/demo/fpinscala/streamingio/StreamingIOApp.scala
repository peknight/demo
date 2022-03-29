package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.iomonad.App.IO
import com.peknight.demo.fpinscala.iomonad.Free.Return

object StreamingIOApp extends App {
  def linesGt40k(filename: String): IO[Boolean] = Return {
    val src = io.Source.fromFile(filename)
    try {
      var count = 0
      val lines: Iterator[String] = src.getLines()
      while (count <= 40000 && lines.hasNext) {
        lines.next()
        count += 1
      }
      count > 40000
    }
    finally src.close()
  }
}
