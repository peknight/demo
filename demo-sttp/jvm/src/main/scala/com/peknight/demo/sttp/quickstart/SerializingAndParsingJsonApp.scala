package com.peknight.demo.sttp.quickstart

import sttp.client4.*
import sttp.client4.upicklejson.default.*
import upickle.default.*

object SerializingAndParsingJsonApp extends App:

  val backend = DefaultSyncBackend()

  case class MyRequest(field1: String, field2: Int)
  // selected fields from the JSON that is being returned by httpbin
  case class HttpBinResponse(origin: String, headers: Map[String, String])

  given ReadWriter[MyRequest] = macroRW[MyRequest]

  given ReadWriter[HttpBinResponse] = macroRW[HttpBinResponse]

  val request = basicRequest
    .post(uri"https://httpbin.org/post")
    .body(asJson(MyRequest("test", 42)))
    .response(asJson[HttpBinResponse])
  val response = request.send(backend)

  response.body match
    case Left(e) => println(s"Got response exception:\n$e")
    case Right(r) => println(s"Origin's ip: ${r.origin}, header count: ${r.headers.size}")
end SerializingAndParsingJsonApp
