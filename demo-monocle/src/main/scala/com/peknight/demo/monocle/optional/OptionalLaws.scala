package com.peknight.demo.monocle.optional

import monocle.Optional

class OptionalLaws[S, A](optional: Optional[S, A]):
  def getOptionSet(s: S)(using CanEqual[S, S]): Boolean =
    optional.getOrModify(s).fold(identity, optional.replace(_)(s)) == s

  def setGetOption(s: S, a: A)(using CanEqual[A, A]): Boolean =
    optional.getOption(optional.replace(a)(s)) == optional.getOption(s).map(_ => a)

