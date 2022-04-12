package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.ExecutorService

/**
 * Provides a function for evaluating expressions, possibly asynchronously.
 * The `apply` function should typically begin evaluating its argument
 * immediately. The returned thunk can be used to block until the resulting `A`
 * is available
 */
trait Strategy {
  def apply[A](a: => A): () => A
}
object Strategy {
  /**
   * We can create a `Strategy` from any `ExecutorService`. It's a little more
   * convenient than submitting `Callable` objects directly
   */
  def fromExecutorService(es: ExecutorService): Strategy = new Strategy {
    def apply[A](a: => A): () => A = {
      val f = es.submit(() => a)
      () => f.get()
    }
  }

  /**
   * A `Strategy` which begins executing its argument immediately in the calling thread.
   */
  def sequential: Strategy = new Strategy {
    def apply[A](a: => A): () => A = {
      val r = a
      () => r
    }
  }
}

