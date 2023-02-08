package com.peknight.demo.scalacheck.concepts

import org.scalacheck.{Arbitrary, Gen}

abstract sealed class TreeV2[T]:
  def merge(t: TreeV2[T]) = TreeV2.Internal(List(this, t))
  def size: Int = this match
    case TreeV2.Leaf(_) => 1
    case TreeV2.Internal(children) => children.foldRight(0)(_.size + _)
end TreeV2
object TreeV2:
  case class Internal[T](children: Seq[TreeV2[T]]) extends TreeV2[T]
  case class Leaf[T](elem: T) extends TreeV2[T]

  given [T: Arbitrary]: Arbitrary[TreeV2[T]] = Arbitrary {
    val genLeaf = for e <- Arbitrary.arbitrary[T] yield Leaf(e)
    def genInternal(sz: Int): Gen[TreeV2[T]] =
      for
        n <- Gen.choose(sz / 3, sz / 2)
        c <- Gen.listOfN(n, sizedTree(sz / 2))
      yield Internal(c)
    def sizedTree(sz: Int) =
      if sz <= 0 then genLeaf
      else Gen.frequency((1, genLeaf), (3, genInternal(sz)))
    Gen.sized(sz => sizedTree(sz))
  }
end TreeV2
