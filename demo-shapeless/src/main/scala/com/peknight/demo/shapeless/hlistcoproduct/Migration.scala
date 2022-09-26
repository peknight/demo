package com.peknight.demo.shapeless.hlistcoproduct

import cats.Monoid
import com.peknight.demo.shapeless.*

import scala.compiletime.constValue
import scala.deriving.Mirror

trait Migration[A, B]:
  def apply(a: A): B

object Migration:
  extension [A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.apply(a)

  type MirrorAux[A, Label <: Tuple, Repr <: Tuple] = Mirror.ProductOf[A] {
    type MirroredElemLabels = Label
    type MirroredElemTypes = Repr
  }
  type GetValue[A] = A match
    case (h, t) => t
  def getValue[A](t: A): GetValue[A] = t match
    case (_, value) => value.asInstanceOf[GetValue[A]]

  // 暂时没找到scala3版本的shapeless.ops.hlist.*
  inline given [A <: Product, ALabel <: Tuple, ARepr <: Tuple, B <: Product, BLabel <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Added <: Tuple, Unaligned <: Tuple] (
    using
    aMirror: MirrorAux[A, ALabel, ARepr],
    bMirror: MirrorAux[B, BLabel, BRepr],
    inter: Intersection.Aux[Tuple.Zip[ALabel, ARepr], Tuple.Zip[BLabel, BRepr], Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[Tuple.Zip[BLabel, BRepr], Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, Tuple.Zip[BLabel, BRepr]]
  ): Migration[A, B] = (a: A) =>
    bMirror.fromProduct(
      align(prepend(monoid.empty, inter(summonValuesAsTuple[ALabel].zip(Tuple.fromProductTyped(a)))))
        .map[GetValue]([T] => (t: T) => getValue(t))
    )

  given Monoid[EmptyTuple] = Monoid.instance(EmptyTuple, (_, _) => EmptyTuple)

  inline given [K <: String, H, T <: Tuple](using hMonoid: => Monoid[H], tMonoid: Monoid[T]): Monoid[(K, H) *: T] =
    val label = constValue[K]
    Monoid.instance(
      (label, hMonoid.empty) *: tMonoid.empty,
      (x, y) => (label, hMonoid.combine(x.head._2, y.head._2)) *: tMonoid.combine(x.tail, y.tail)
    )
