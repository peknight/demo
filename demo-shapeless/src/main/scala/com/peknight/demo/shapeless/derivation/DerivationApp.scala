package com.peknight.demo.shapeless.derivation

import com.peknight.demo.shapeless.derivation.Opt.*

object DerivationApp extends App:
  def test(): Unit =
    val eqoi = summon[Eq[Opt[Int]]]
    assert(eqoi.eqv(Sm(23), Sm(23)))
    assert(!eqoi.eqv(Sm(23), Sm(13)))
    assert(!eqoi.eqv(Sm(23), Nn))
    val eqTree = summon[Eq[Tree[Int]]]
    assert(eqTree.eqv(Tree.Branch(Tree.Leaf(1), Tree.Leaf(2)), Tree.Branch(Tree.Leaf(1), Tree.Leaf(2))))
    assert(!eqTree.eqv(Tree.Branch(Tree.Leaf(3), Tree.Leaf(2)), Tree.Branch(Tree.Leaf(1), Tree.Leaf(2))))

  def testShapeless(): Unit =
    val eqoi = summon[EqShapeless[Opt[Int]]]
    assert(eqoi.eqv(Sm(53), Sm(53)))
    assert(!eqoi.eqv(Sm(53), Sm(33)))
    assert(!eqoi.eqv(Sm(53), Nn))
    val eqTree = summon[EqShapeless[Tree[Int]]]
    assert(eqTree.eqv(Tree.Branch(Tree.Leaf(10), Tree.Leaf(20)), Tree.Branch(Tree.Leaf(10), Tree.Leaf(20))))
    assert(!eqTree.eqv(Tree.Branch(Tree.Leaf(30), Tree.Leaf(20)), Tree.Branch(Tree.Leaf(10), Tree.Leaf(20))))

  test()
  testShapeless()

