package com.peknight.demo.scalatest.async

import com.peknight.demo.scalatest.async.StringOp.*

import scala.concurrent.{ExecutionContext, Future}

// Simulating an actor
class StringActor:
  private final val sb = new StringBuilder
  def !(op: StringOp): Unit = synchronized { op match
    case Append(value) => sb.append(value)
    case Clear => sb.clear()
  }

  def ?(get: GetValue.type)(using c: ExecutionContext): Future[String] = Future { synchronized {sb.toString } }
end StringActor

