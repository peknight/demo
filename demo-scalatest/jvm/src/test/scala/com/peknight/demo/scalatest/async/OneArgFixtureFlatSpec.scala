package com.peknight.demo.scalatest.async

import org.scalatest.*
import org.scalatest.flatspec.FixtureAsyncFlatSpec
import com.peknight.demo.scalatest.async.StringOp.*

import java.io.File
import scala.concurrent.Future

class OneArgFixtureFlatSpec extends FixtureAsyncFlatSpec:

  type FixtureParam = StringActor

  def withFixture(test: OneArgAsyncTest): FutureOutcome =
    val actor = new StringActor
    complete {
      // set up the fixture
      actor ! Append("ScalaTest is ")
      withFixture(test.toNoArgAsyncTest(actor))
    } lastly {
      // ensure the fixture will be cleaned up
      actor ! Clear
    }

  "Testing" should "be easy" in { actor =>
    actor ! Append("easy!")
    val futureString = actor ? GetValue
    futureString map { s =>
      assert(s == "ScalaTest is easy!")
    }
  }

  it should "be fun" in { actor =>
    actor ! Append("fun!")
    val futureString = actor ? GetValue
    futureString map { s =>
      assert(s == "ScalaTest is fun!")
    }
  }