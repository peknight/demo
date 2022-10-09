package com.peknight.demo.shapeless2.hlistcoproduct

import cats.Monoid
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist.{Align, Diff, Intersection, Prepend}
import shapeless.{::, HList, HNil, LabelledGeneric, Lazy}

trait Migration[A, B] {
  def apply(a: A): B
}
object Migration {
  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B = migration.apply(a)
  }

  implicit def genericMigration[
    A, B,
    ARepr <: HList, BRepr <: HList,
    Common <: HList, Added <: HList,
    Unaligned <: HList
  ](
    implicit
    aGen: LabelledGeneric.Aux[A, ARepr],
    bGen: LabelledGeneric.Aux[B, BRepr],
    inter: Intersection.Aux[ARepr, BRepr, Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[BRepr, Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, BRepr]
  ): Migration[A, B] = (a: A) => bGen.from(align.apply(prepend(monoid.empty, inter.apply(aGen.to(a)))))

  def createMonoid[A](zero: A)(add: (A, A) => A): Monoid[A] = new Monoid[A] {
    def empty = zero
    def combine(x: A, y: A): A = add(x, y)
  }

  implicit val hnilMonoid: Monoid[HNil] = createMonoid[HNil](HNil)((_, _) => HNil)

  implicit def emptyHList[K <: Symbol, H, T <: HList](implicit hMonoid: Lazy[Monoid[H]], tMonoid: Monoid[T])
  : Monoid[FieldType[K, H] :: T] =
    createMonoid(field[K](hMonoid.value.empty) :: tMonoid.empty) { (x, y) =>
      field[K](hMonoid.value.combine(x.head, y.head)) :: tMonoid.combine(x.tail, y.tail)
    }
}
