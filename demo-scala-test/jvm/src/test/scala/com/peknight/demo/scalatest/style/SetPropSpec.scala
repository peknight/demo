package com.peknight.demo.scalatest.style

import org.scalatest.*
import org.scalatest.matchers.*
import org.scalatest.prop.*
import org.scalatest.propspec.AnyPropSpec

import scala.collection.immutable.*

/**
 * The PropSpec style
 *
 * The AnyPropSpec style is perfect for teams that want to write tests exclusively in terms of property checks; also a
 * good choice for writing the occasional test matrix when a different style trait is chosen as the main unit testing
 * style
 *
 * To select just the PropSpec style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-propspec" % version % Test
 */
class SetPropSpec extends AnyPropSpec with TableDrivenPropertyChecks with should.Matchers:
  val examples = Table("set", BitSet.empty, HashSet.empty[Int], TreeSet.empty[Int])
  property("an empty Set should have size 0") {
    forAll(examples) { set => set.size should be (0) }
  }
  property("invoking head on an empty set should produce NoSuchElementException") {
    forAll(examples) { set =>
      a [NoSuchElementException] should be thrownBy { set.head }
    }
  }

