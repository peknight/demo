package com.peknight.demo.scalatest.style

import org.scalatest.refspec.RefSpec

/**
 * RefSpec (JVM only)
 *
 * The RefSpec style allows you to define tests as methods, which saves one function literal per test compared to style
 * classes that represent tests as functions. Fewer function literals translates into faster compile times and fewer
 * generated class files, which can help minimize build times. As a result, using Spec can be a good choice in large
 * projects where build times are a concern as well as when generating large numbers of tests programmatically via
 * static code generators.
 *
 * Note: The "Ref" in RefSpec stands for reflection, which RefSpec uses to discover tests. As reflection is not
 * available in Scala.js, this class is not available on Scala.js.
 *
 * To Select just the RefSpec style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-refspec" % version % Test
 */
class SetRefSpec extends RefSpec:
  object `A Set` {
    object `when Empty` {
      def `should have size 0`(): Unit = {
        assert(Set.empty.size == 0)
      }
      def `should produce NoSuchElementException when head is invoked`(): Unit = {
        assertThrows[NoSuchElementException] { Set.empty.head }
      }
    }
  }
