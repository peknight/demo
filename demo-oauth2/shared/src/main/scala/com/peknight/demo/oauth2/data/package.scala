package com.peknight.demo.oauth2

import cats.Functor
import cats.data.{Chain, OptionT}
import cats.syntax.functor.*
import org.http4s.{Uri, UrlForm}

package object data:
  extension[F[_] : Functor, A] (fa: F[A])
    def optionT = OptionT(fa.map(Option.apply))
  end extension
