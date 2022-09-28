package com.peknight.demo.shapeless.generic

import cats.{Id, Monoid}
import com.peknight.demo.shapeless.generic.*

import scala.compiletime.constValue

trait Migration[A, B]:
  def apply(a: A): B
end Migration

object Migration:

  extension [A] (a: A)
    def migrateTo[B](using mapper: Migration[A, B]): B = mapper.apply(a)

  inline given [A <: Product, Repr <: Tuple] (using mirror: MirrorProductAux[A, Repr]): Migration[A, Repr] with
    def apply(a: A): Repr = Tuple.fromProductTyped(a)

  inline given [A <: Product, Repr <: Tuple] (using mirror: MirrorProductAux[A, Repr]): Migration[Repr, A] with
    def apply(a: Repr): A = mirror.fromProduct(a)

  type GetValue[A] = A match
    case (h, t) => t

  inline given [A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Added <: Tuple, Unaligned <: Tuple] (
    using
    aMirror: MirrorProductLabelledAux[A, ALabels, ARepr],
    bMirror: MirrorProductLabelledAux[B, BLabels, BRepr],
    inter: Intersection.Aux[Tuple.Zip[ALabels, ARepr], Tuple.Zip[BLabels, BRepr], Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[Tuple.Zip[BLabels, BRepr], Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, Tuple.Zip[BLabels, BRepr]]
  ): Migration[A, B] = (a: A) => bMirror.fromProduct(
    align(prepend(monoid.empty, inter(summonValuesAsTuple[ALabels].zip(Tuple.fromProductTyped(a))))).map[GetValue] {
      [T] => (t: T) => t match
        case (_, value) => value.asInstanceOf[GetValue[T]]
    }
  )

  given Monoid[EmptyTuple] = Monoid.instance(EmptyTuple, (_, _) => EmptyTuple)

  inline given [K <: String, H, T <: Tuple](using hMonoid: => Monoid[H], tMonoid: Monoid[T]): Monoid[(K, H) *: T] =
    val label = constValue[K]
    Monoid.instance(
      (label, hMonoid.empty) *: tMonoid.empty,
      (x, y) => (label, hMonoid.combine(x.head._2, y.head._2)) *: tMonoid.combine(x.tail, y.tail)
    )
