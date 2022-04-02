package com.peknight.demo.catseffect.testruntime

import cats.effect.IO
import cats.effect.std.Random
import cats.effect.testkit.TestControl
import munit.CatsEffectSuite

import scala.concurrent.duration.DurationInt

class MockingTimeTest extends CatsEffectSuite {
  test("retry at least 3 times until success") {
    case object TestException extends RuntimeException

    var attempts = 0
    val action = IO {
      attempts += 1
      if (attempts != 3) throw TestException
      else "success!"
    }
    val program = Random.scalaUtilRandom[IO].flatMap { random => MockingTimeApp.retry(action, 1.minute, 3, random) }

    // 用TestControl可以光速度过sleep时间完成测试
    TestControl.executeEmbed(program).assertEquals("success!")
  }
}
