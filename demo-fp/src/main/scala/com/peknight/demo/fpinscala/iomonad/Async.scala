package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.Async.*
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import scala.annotation.tailrec

/**
 * Free[Par, A]
 */
sealed trait Async[A]:
  def flatMap[B](f: A => Async[B]): Async[B] = FlatMap(this, f)
  def map[B](f: A => B): Async[B] = flatMap(f andThen (Return(_)))

object Async:

  case class Return[A](a: A) extends Async[A]
  case class Suspend[A](resume: Par[A]) extends Async[A]
  case class FlatMap[A, B](sub: Async[A], k: A => Async[B]) extends Async[B]

  //noinspection DuplicatedCode
  @tailrec def step[A](async: Async[A]): Async[A] = async match
    case FlatMap(FlatMap(x, f), g) => step(x.flatMap(a => f(a).flatMap(g)))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => async

  def run[A](async: Async[A]): Par[A] = step(async) match
    case Return(a) => Par.unit(a)
    case Suspend(r) => r
    case FlatMap(x, f) => x match
      case Suspend(r) => Par.flatMap(r)(a => run(f(a)))
      case _ => sys.error("Impossible; `step` eliminates these cases")
