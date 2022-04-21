package com.peknight.demo.fpinscala.parallelism

import com.peknight.demo.fpinscala.parallelism.Actor.Node

import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import scala.annotation.tailrec

/*
 * Implementation is taken from `scalaz` library, with only minor changes. See:
 *
 * https://github.com/scalaz/scalaz/blob/scalaz-seven/concurrent/src/main/scala/scalaz/concurrent/Actor.scala
 *
 * This code is copyright Andriy Plokhotnyuk, Runar Bjarnason, and other contributors,
 * and is licensed using 3-clause BSD, see LICENSE file at:
 *
 * https://github.com/scalaz/scalaz/blob/scalaz-seven/etc/LICENCE
 */

/**
 * Processes messages of type `A`, one at a time. Messages are submitted to
 * the actor with the method `!`. Processing is typically performed asynchronously,
 * this is controlled by the provided `strategy`.
 *
 * Memory consistency guarantee: when each message is processed by the `handler`, any memory that it
 * mutates is guaranteed to be visible by the `handler` when it processes the next message, even if
 * the `strategy` runs the invocations of `handler` on separate threads. This is achieved because the `Actor`
 * reads a volatile memory location before entering its event loop, and writes to the same location
 * before suspending.
 *
 * Implementation based on non-intrusive MPSC node-based queue, described by Dmitriy Vyukov:
 * [[http://www.1024cores.net/home/lock-free-algorithms/queues/non-intrusive-mpsc-node-based-queue]]
 *
 * @see scalaz.concurrent.Promise for a use case.
 *
 * @param handler  The message handler
 * @param onError  Exception handler, called if the message handler throws any `Throwable`.
 * @param strategy Execution strategy, for example, a strategy that is backed by an `ExecutorService`
 * @tparam A       The type of messages accepted by this actor.
 */
final class Actor[A](strategy: Strategy)(handler: A => Unit, onError: Throwable => Unit = throw(_)):
  self =>

  // 一定要自己画图理解
  private val tail = new AtomicReference(new Node[A]())
  private val suspended = new AtomicInteger(1)
  private val head = new AtomicReference(tail.get)

  /** Alias for `apply` */
  def !(a: A): Unit =
    val n = new Node(a)
    head.getAndSet(n).lazySet(n)
    trySchedule()

  /** Pass the message `a` to the mailbox of this actor */
  def apply(a: A): Unit = this ! a

  def contramap[B](f: B => A): Actor[B] = new Actor(strategy)((b: B) => this ! f(b), onError)

  private def trySchedule(): Unit = if suspended.compareAndSet(1, 0) then schedule()

  private def schedule(): Unit = strategy(act())

  private def act(): Unit =
    val t = tail.get()
    val n = batchHandle(t, 1024)
    if n ne t then
      n.a = null.asInstanceOf[A]
      tail.lazySet(n)
      schedule()
    else
      suspended.set(1)
      if n.get ne null then trySchedule()

  @tailrec private def batchHandle(t: Node[A], i: Int): Node[A] =
    val n = t.get
    if n ne null then
      try handler(n.a)
      catch case ex: Throwable => onError(ex)
      if i > 0 then batchHandle(n, i - 1) else n
    else t

end Actor


object Actor:

  private class Node[A](var a: A = null.asInstanceOf[A]) extends AtomicReference[Node[A]]

  def apply[A](es: ExecutorService)(handler: A => Unit, onError: Throwable => Unit = throw(_)): Actor[A] =
    new Actor(Strategy.fromExecutorService(es))(handler, onError)

end Actor

