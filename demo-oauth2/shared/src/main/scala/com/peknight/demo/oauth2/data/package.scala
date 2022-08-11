package com.peknight.demo.oauth2

import cats.Functor
import cats.data.OptionT
import cats.syntax.functor.*

package object data:
  extension[F[_] : Functor, A] (fa: F[A])
    def optionT = OptionT(fa.map(Option.apply))
