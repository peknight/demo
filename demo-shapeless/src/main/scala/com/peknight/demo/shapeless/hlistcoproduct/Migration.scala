package com.peknight.demo.shapeless.hlistcoproduct

import shapeless3.deriving.K0

trait Migration[A, B]:
  def apply(a: A): B

object Migration:
  extension [A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.apply(a)

  // 暂时没找到scala3版本的shapeless.ops.hlist.*

