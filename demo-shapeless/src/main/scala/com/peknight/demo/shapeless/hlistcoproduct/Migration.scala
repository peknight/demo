package com.peknight.demo.shapeless.hlistcoproduct

import shapeless3.deriving.K0

trait Migration[A, B]:
  def apply(a: A): B

object Migration:
  extension [A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.apply(a)

