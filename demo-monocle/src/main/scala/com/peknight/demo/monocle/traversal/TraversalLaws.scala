package com.peknight.demo.monocle.traversal

import monocle.Traversal

object TraversalLaws:
  def modifyGetAll[S, A](t: Traversal[S, A], s: S, f: A => A)(using CanEqual[A, A]): Boolean =
    t.getAll(t.modify(f)(s)) == t.getAll(s).map(f)

  def composeModify[S, A](t: Traversal[S, A], s: S, f: A => A, g: A => A)(using CanEqual[S, S]): Boolean =
    t.modify(g)(t.modify(f)(s)) == t.modify(g compose f)(s)

