package com.peknight.demo.shapeless.hlistcoproduct

import shapeless.ops.hlist.Intersection
import shapeless.{HList, LabelledGeneric}

trait Migration[A, B] {
  def apply(a: A): B
}
object Migration {
  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B = migration.apply(a)
  }

  implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList](implicit aGen: LabelledGeneric.Aux[A, ARepr],
                                                                      bGen: LabelledGeneric.Aux[B, BRepr],
                                                                      inter: Intersection.Aux[ARepr, BRepr, BRepr])
  : Migration[A, B] = (a: A) => bGen.from(inter.apply(aGen.to(a)))
}
