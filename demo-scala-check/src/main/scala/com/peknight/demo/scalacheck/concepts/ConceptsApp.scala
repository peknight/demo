package com.peknight.demo.scalacheck.concepts

import org.scalacheck.Prop.{all, atLeastOne, classify, collect, forAll, propBoolean}
import org.scalacheck.{Arbitrary, Gen}

object ConceptsApp extends App:
  val propReverseList = forAll { (l: List[String]) => l.reverse.reverse == l }
  propReverseList.check()
  val propConcatString = forAll { (s1: String, s2: String) => (s1 + s2).endsWith(s2) }
  propConcatString.check()

  val smallInteger = Gen.choose(0, 100)
  val propSmallInteger = forAll(smallInteger) { n => n >= 0 && n <= 100 }
  propSmallInteger.check()

  // Conditional Properties
  val propMakeList = forAll { (n: Int) =>
    (n >= 0 && n < 10000) ==> (List.fill(n)("").length == n)
  }
  propMakeList.check()

  val propTrivial = forAll { (n: Int) =>
    (n == 0) ==> (n == 0)
  }
  propTrivial.check()

  // Combining Properties
  val p3 = propReverseList && propConcatString
  p3.check()

  val p4 = propReverseList || propConcatString
  p4.check()

  val p5 = propReverseList == propConcatString
  p5.check()

  // same as p3
  val p6 = all(propReverseList, propConcatString)
  p6.check()

  // smae as p4
  val p7 = atLeastOne(propReverseList, propConcatString)
  p7.check()

  StringSpecification.check()
  // MyAppSpecification.check()

  // Labeling Properties
  def myMagicFunction(n: Int, m: Int) = n + m
  val complexProp = forAll { (m: Int, n: Int) =>
    val res = myMagicFunction(n, m)
    (res >= m) :| "result > #1" &&
      (res >= n) :| "result > #2" &&
      (res < m + n) :| "result not sum"
  }
  complexProp.check()

  val complexProp2 = forAll { (m: Int, n: Int) =>
    val res = myMagicFunction(n, m)
    ("result > #1" |: res >= m) &&
      ("result > #2" |: res >= n) &&
      ("result not sum" |: res < m + n)
  }
  complexProp2.check()

  val propMul = forAll { (n: Int, m: Int) =>
    val res = n * m
    (s"evidence = $res") |: all(
      "div1" |: m != 0 ==> (res / m == n),
      "div2" |: n != 0 ==> (res / n == m),
      "lt1" |: res > m,
      "lt2" |: res > n
    )
  }
  propMul.check()

  // Generators
  
  val myGen =
    for
      n <- Gen.choose(10, 20)
      m <- Gen.choose(2 * n, 500)
    yield (n, m)

  val vowel = Gen.oneOf('A', 'E', 'I', 'O', 'U', 'Y')

  val vowelFrequency = Gen.frequency(
    (3, 'A'),
    (4, 'E'),
    (2, 'I'),
    (3, 'O'),
    (1, 'U'),
    (1, 'Y')
  )
  
  // Generating Case Classes
  
  val genLeaf = Gen.const(Tree.Leaf)
  val genNode =
    for
      v <- Arbitrary.arbitrary[Int]
      left <- genTree
      right <- genTree
    yield Tree.Node(left, right, v)

  def genTree: Gen[Tree] = Gen.oneOf(genLeaf, Gen.lzy(genNode))
  println(genTree.sample)

  def matrix[T](g: Gen[T]): Gen[Seq[Seq[T]]] = Gen.sized { size =>
    val side = scala.math.sqrt(size).asInstanceOf[Int]
    Gen.listOfN(side, Gen.listOfN(side, g))
  }
  println(matrix(vowel).sample)

  // Conditional Generators

  val smallEvenInteger = Gen.choose(0, 200).suchThat(_ % 2 == 0)
  println(smallEvenInteger.sample)

  // Generating Containers

  val genIntList = Gen.containerOf[List, Int](Gen.oneOf(1, 3, 5))
  println(genIntList.sample)
  val genStringLazyList = Gen.containerOf[LazyList, String](Gen.alphaStr)
  println(genStringLazyList.sample)
  val genBoolArray = Gen.containerOf[Array, Boolean](true)
  println(genBoolArray.sample)

  val zeroOrMoreDigits = Gen.someOf(1 to 9)
  println(zeroOrMoreDigits.sample)
  val oneOrMoreDigits = Gen.atLeastOne(1 to 9)
  println(oneOrMoreDigits.sample)

  val fiveDice: Gen[collection.Seq[Int]] = Gen.pick(5, 1 to 6)
  println(fiveDice.sample)
  val threeLetters: Gen[collection.Seq[Char]] = Gen.pick(3, 'A' to 'Z')
  println(threeLetters.sample)

  val threeLettersPermuted = threeLetters.map(scala.util.Random.shuffle(_))
  println(threeLettersPermuted.sample)

  // The arbitrary Generator

  val evenInteger = Arbitrary.arbitrary[Int].suchThat(_ % 2 == 0)
  println(evenInteger.sample)
  val squares =
    for
      xs <- Arbitrary.arbitrary[List[Int]]
    yield xs.map(x => (x, x * x))
  println(squares.sample)

  val propMergeTree = forAll((t1: TreeV2[Int], t2: TreeV2[Int]) => t1.size + t2.size == t1.merge(t2).size)
  propMergeTree.check()

  // Collecting Generated Test Data

  def ordered(l: List[Int]) = l == l.sorted

  val myProp = forAll { (l: List[Int]) =>
    classify(ordered(l), "ordered") {
      classify(l.length > 5, "large", "small") {
        l.reverse.reverse == l
      }
    }
  }
  myProp.check()

  val dummyProp = forAll(Gen.choose(1, 10)) { n =>
    collect(n) { n == n }
  }
  dummyProp.check()

  CounterSpecification.property().check()