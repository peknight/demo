package com.peknight.demo.catseffect.iolocal

import cats.effect.Sync
import cats.effect.std.Random
import cats.syntax.flatMap.*
import cats.syntax.functor.*

case class TraceId(value: String)
object TraceId:
  def gen[F[_] : Sync]: F[TraceId] = Random.scalaUtilRandom[F].flatMap(_.nextString(8)).map(TraceId.apply)
