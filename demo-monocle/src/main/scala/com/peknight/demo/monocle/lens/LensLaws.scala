package com.peknight.demo.monocle.lens

import monocle.Lens

object LensLaws:
  def getReplace[S, A](l: Lens[S, A], s: S)(using CanEqual[S, S]): Boolean =
    l.replace(l.get(s))(s) == s

  def replaceGet[S, A](l: Lens[S, A], s: S, a: A)(using CanEqual[A, A]): Boolean =
    l.get(l.replace(a)(s)) == a
