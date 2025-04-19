package com.peknight.demo.sttp.quickstart

import sttp.client4.*

object SynchronousRequestsApp extends App:
  val backend = DefaultSyncBackend()
  val response = basicRequest
    .body("Hello, world!")
    .post(uri"https://httpbin.org/post?hello=world")
    .send(backend)
  println(response.body)
end SynchronousRequestsApp
