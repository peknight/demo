package com.peknight.demo.shapeless.derivation

import scala.compiletime.{erasedValue, summonInline}
import scala.deriving.Mirror

trait Eq[T]:
  def eqv(x: T, y: T): Boolean

object Eq:

  given Eq[Int] with
    def eqv(x: Int, y: Int) = x == y

  def check(elem: Eq[?])(x: Any, y: Any): Boolean =
    elem.asInstanceOf[Eq[Any]].eqv(x, y)

  def iterator[T](p: T) = LazyList.from(p.asInstanceOf[Product].productIterator)

  def eqSum[T](s: Mirror.SumOf[T], elems: LazyList[Eq[?]]): Eq[T] = (x: T, y: T) =>
    val ordx = s.ordinal(x)
    (s.ordinal(y) == ordx) && check(elems(ordx))(x, y)

  def eqProduct[T](p: Mirror.ProductOf[T], elems: LazyList[Eq[?]]): Eq[T] = (x: T, y: T) =>
    iterator(x).zip(iterator(y)).zip(elems).forall {
      case ((x, y), elem) => check(elem)(x, y)
    }

  inline given derived[T](using m: Mirror.Of[T]): Eq[T] =
    val elemInstances = summonAll[m.MirroredElemTypes]
    inline m match
      case s: Mirror.SumOf[T] => eqSum(s, elemInstances)
      case p: Mirror.ProductOf[T] => eqProduct(p, elemInstances)

  inline def summonAll[T <: Tuple]: LazyList[Eq[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => LazyList.empty[Eq[?]]
      case _: (t *: ts) => summonInline[Eq[t]] #:: summonAll[ts]

end Eq