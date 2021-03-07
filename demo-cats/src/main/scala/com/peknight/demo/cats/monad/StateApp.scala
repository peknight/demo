package com.peknight.demo.cats.monad

import cats.data.State

object StateApp extends App {

  val a = State[Int, String] { state =>
    (state, s"The state is $state")
  }

  val (state, result) = a.run(10).value
  println(state)
  println(result)

  val justTheState = a.runS(10).value
  println(justTheState)

  val justTheResult = a.runA(10).value
  println(justTheResult)

  val step1 = State[Int, String] { num =>
    val ans = num + 1
    (ans, s"Result of step1: $ans")
  }

  val step2 = State[Int, String] { num =>
    val ans = num * 2
    (ans, s"Result of step2: $ans")
  }

  val both = for {
    a <- step1
    b <- step2
  } yield (a, b)

  val (stateBoth, resultBoth) = both.run(20).value
  println(stateBoth)
  println(resultBoth)

  val getDemo = State.get[Int]
  println(getDemo.run(10).value)

  val setDemo = State.set[Int](30)
  println(setDemo.run(10).value)

  val pureDemo = State.pure[Int, String]("Result")
  println(pureDemo.run(10).value)

  val inspectDemo = State.inspect[Int, String](x => s"${x}!")
  println(inspectDemo.run(10).value)

  val modifyDemo = State.modify[Int](_ + 1)
  println(modifyDemo.run(10).value)

  val program: State[Int, (Int, Int, Int)] = for {
    a <- State.get[Int]
    _ <- State.set[Int](a + 1)
    b <- State.get[Int]
    _ <- State.modify[Int](_ + 1)
    c <- State.inspect[Int, Int](_ * 1000)
  } yield (a, b, c)
  val (stateProgram, resultProgram) = program.run(1).value
  println(stateProgram)
  println(resultProgram)
}
