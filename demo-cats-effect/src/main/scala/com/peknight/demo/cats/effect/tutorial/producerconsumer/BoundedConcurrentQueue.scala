package com.peknight.demo.cats.effect.tutorial.producerconsumer

/**
 * Specific methods for bounded concurrent queues.
 */
trait BoundedConcurrentQueue[F[_], A] extends ConcurrentQueue[F, A] {
  /** Max queue size. */
  val maxQueueSize: Int
  /** Remaining empty buckets. Non-blocking.*/
  def emptyBuckets: F[Long]
  /** Returns `F[true]` if queue full, `F[false]` otherwise. Non-blocking. */
  def isFull: F[Boolean]
}
