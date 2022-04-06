package com.peknight.demo.catseffect.testruntime

import cats.Id
import cats.effect.std.Random
import cats.effect.testkit.TestControl
import cats.effect.{IO, Outcome}
import cats.syntax.all._
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
    val program = Random.scalaUtilRandom[IO].flatMap { random => MockingTimeApp.retry(action, 1.minute, 5, random) }

    // 用TestControl可以光速度过sleep时间完成测试
    TestControl.executeEmbed(program).assertEquals("success!")
  }

  test("backoff appropriately between attempts") {
    case object TestException extends RuntimeException

    val action = IO.raiseError[String](TestException)
    val program = Random.scalaUtilRandom[IO].flatMap { random => MockingTimeApp.retry(action, 1.minute, 5, random) }

    TestControl.execute(program).flatMap { control =>
      for {
        _ <- control.results.assertEquals(None)
        /*
         * `tick`会让`program`运行至所有的fibers都进入`sleep`或`program`完成。
         * 在这个场景里，只有一个fiber，并会在首次尝试运行`action`后立即进入`sleep`状态，`tick`运行至此即停止
         * `tick`并不会推进时间。一旦它到达所有fibers都进入`sleep`等待时间推进的节点后，它将控制权交还给调用者，允许我们对当前状态进行检查
         */
        _ <- control.tick
        // 这里我们可以再次调用`result`但是此时`program`肯定还没有执行完成。
        // 我们也可以检查`isDeadlocked`判断是否所有fibers都被hung而非`sleep`（当然结果也会是`false`）

        /*
         * 我们可以使用更有用的`nextInterval`。
         * 当所有活动fibers都进入睡眠时，`TestControl`必须推进所有活动fibers中最小程度的睡眠时间使其中任意一个继续执行
         * 在这个场景里（每次`action`都会抛出异常进入二进制指数退避），正是我们当前退避的时间，在0到当前的`delay`之间
         */
        _ <- 0.until(4).toList.traverse { i =>
          for {
            _ <- control.results.assertEquals(None)
            interval <- control.nextInterval
            _ <- IO(assert(interval >= 0.nanos))
            _ <- IO(assert(interval < (1 << i).minute))
            // 推进时间并执行至下一个tick完成
            _ <- control.advanceAndTick(interval)
          } yield ()
        }
        // 文档中使用的Outcome.failed(TestException)的语法没找到方式使其编译通过，这里改为使用Outcome.Errored
        _ <- control.results.assertEquals(Some(Outcome.Errored[Id, Throwable, String](TestException)))
      } yield ()
    }
  }
}
