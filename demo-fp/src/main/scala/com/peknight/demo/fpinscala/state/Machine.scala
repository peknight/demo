package com.peknight.demo.fpinscala.state

import com.peknight.demo.fpinscala.state.State.*

case class Machine(locked: Boolean, candies: Int, coins: Int)

object Machine:
  sealed trait Input derives CanEqual
  case object Coin extends Input
  case object Turn extends Input

  def update = (i: Input) => (s: Machine) => (i, s) match
    case (_, Machine(_, 0, _)) => s
    case (Coin, Machine(false, _, _)) => s
    case (Turn, Machine(true, _, _)) => s
    case (Coin, Machine(true, candies, coins)) => Machine(false, candies, coins + 1)
    case (Turn, Machine(false, candies, coins)) => Machine(true, candies - 1, coins)

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] =
    for
      // 精(tuo1)髓(fa4)的modify _ compose
      _ <- sequence(inputs.map(modify[Machine] compose update))
      s <- get
    yield (s.coins, s.candies)