package com.peknight.demo.scalacheck.concepts

import org.scalacheck.{Gen, Prop}
import org.scalacheck.commands.Commands

import scala.util.{Success, Try}

object CounterSpecification extends Commands:
  given CanEqual[Try[Int], Success[Int]] = CanEqual.derived
  type State = Int
  type Sut = Counter

  def canCreateNewSut(newState: Int, initSuts: Iterable[Int], runningSuts: Iterable[Counter]): Boolean = true

  def initialPreCondition(state: Int): Boolean = state == 0

  def newSut(state: Int): Counter = new Counter

  def destroySut(sut: Counter): Unit = ()

  def genInitialState: Gen[Int] = Gen.const(0)

  def genCommand(state: Int): Gen[CounterSpecification.Command] = Gen.oneOf(Inc, Get, Dec, Reset)

  case object Inc extends UnitCommand:
    def run(sut: Sut): Unit = sut.inc
    def nextState(state: State): State = state + 1
    def preCondition(state: State): Boolean = true
    def postCondition(state: State, success: Boolean): Prop = success
  end Inc

  case object Dec extends UnitCommand:
    def run(sut: Sut): Unit = sut.dec
    def nextState(state: State): State = state - 1
    def preCondition(state: State): Boolean = true
    def postCondition(state: State, success: Boolean): Prop = success
  end Dec

  case object Reset extends UnitCommand:
    def run(sut: Sut): Unit = sut.reset
    def nextState(state: State): State = 0
    def preCondition(state: State): Boolean = true
    def postCondition(state: State, success: Boolean): Prop = success
  end Reset

  case object Get extends Command:
    type Result = Int
    def run(sut: Sut): Result = sut.get
    def nextState(state: State): State = state
    def preCondition(state: State): Boolean = true
    def postCondition(state: State, result: Try[Result]): Prop = result == Success(state)
  end Get
