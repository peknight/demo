package com.peknight.demo.sttp.quickstart

import sttp.client4.quick.*

object EvenQuickerApp extends App:
  val response = quickRequest.get(uri"http://httpbin.org/ip").send()
  println(response)
end EvenQuickerApp
