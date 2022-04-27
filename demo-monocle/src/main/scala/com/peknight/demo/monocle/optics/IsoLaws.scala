package com.peknight.demo.monocle.optics

import monocle.Iso

object IsoLaws:
  def roundTripOneWay[S, A](i: Iso[S, A], s: S)(using CanEqual[S, S]): Boolean = i.reverseGet(i.get(s)) == s
  def roundTripOtherWay[S, A](i: Iso[S, A], a: A)(using CanEqual[A, A]): Boolean = i.get(i.reverseGet(a)) == a


