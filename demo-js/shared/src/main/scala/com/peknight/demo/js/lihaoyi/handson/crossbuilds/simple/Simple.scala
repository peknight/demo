package com.peknight.demo.js.lihaoyi.handson.crossbuilds.simple

object Simple:
  def formatTimes(timestamps: Seq[Long]): Seq[String] =
    // Platform在IDEA会报红，实际上Platform分别定义在js与jvm目录下的同名包中，在sbt中运行良好
    timestamps.map(Platform.format).map(_.dropRight(5))
