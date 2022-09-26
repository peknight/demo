package com.peknight.demo.shapeless.generic.mapper.identity

import cats.{Id, Monoid}
import com.peknight.demo.shapeless.generic.*

import scala.compiletime.constValue

trait Mapper[A, B] extends com.peknight.demo.shapeless.generic.mapper.Mapper[Id, A, B]

object Mapper:

  extension [A] (a: A)
    def to[B](using mapper: Mapper[A, B]): B = mapper.to(a)

  type GetValue[A] = A match
    case (h, t) => t

  def getValue[A](t: A): GetValue[A] = t match
    case (_, value) => value.asInstanceOf[GetValue[A]]

  inline given [A <: Product, Repr <: Tuple] (using mirror: MirrorProductAux[A, Repr]): Mapper[A, Repr] with
    def to(a: A): Repr = Tuple.fromProductTyped(a)

  inline given [A <: Product, Repr <: Tuple] (using mirror: MirrorProductAux[A, Repr]): Mapper[Repr, A] with
    def to(a: Repr): A = mirror.fromProduct(a)

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
  ): Mapper[A, B] = (a: A) =>
    bMirror.fromProduct(
      align(prepend(monoid.empty, inter(summonValuesAsTuple[ALabels].zip(Tuple.fromProductTyped(a)))))
        .map[GetValue]([T] => (t: T) => getValue(t))
    )

  given Monoid[EmptyTuple] = Monoid.instance(EmptyTuple, (_, _) => EmptyTuple)

  inline given [K <: String, H, T <: Tuple](using hMonoid: => Monoid[H], tMonoid: Monoid[T]): Monoid[(K, H) *: T] =
    val label = constValue[K]
    Monoid.instance(
      (label, hMonoid.empty) *: tMonoid.empty,
      (x, y) => (label, hMonoid.combine(x.head._2, y.head._2)) *: tMonoid.combine(x.tail, y.tail)
    )
