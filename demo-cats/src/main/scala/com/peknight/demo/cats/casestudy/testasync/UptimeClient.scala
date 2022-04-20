package com.peknight.demo.cats.casestudy.testasync

trait UptimeClient[F[_]]:
  def getUptime(hostname: String): F[Int]
