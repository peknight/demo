package com.peknight.demo.fpinscala.iomonad

/**
 * Translate between any 'F[A]' to 'G[A]'
 */
trait Translate[F[_], G[_]] {
  def apply[A](f: F[A]): G[A]
}
object Translate {
  // This gives us infix syntax F -> G for Translate[F, G]
  type ~>[F[_], G[_]] = Translate[F, G]
}
