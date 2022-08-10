package com.peknight.demo.oauth2

import cats.Applicative
import cats.effect.std.Random
import cats.syntax.functor.*
import cats.syntax.traverse.*

package object random:

  def randomString[F[_] : Applicative](random: Random[F], length: Int): F[String] =
    List.fill(length)(random.nextAlphaNumeric).sequence.map(_.mkString)
