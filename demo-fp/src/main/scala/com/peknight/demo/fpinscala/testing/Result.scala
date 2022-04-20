package com.peknight.demo.fpinscala.testing

sealed trait Result derives CanEqual:
  def isFalsified: Boolean

object Result:

  type FailedCase = String
  type SuccessCount = Int

  case object Passed extends Result:
    def isFalsified = false

  case class Falsified(failure: FailedCase, successes: SuccessCount) extends Result:
    def isFalsified = true

  case object Proved extends Result:
    def isFalsified = true
