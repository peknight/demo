package com.peknight.demo.catseffect.monadcancel

import cats.effect.{IO, IOApp, MonadCancel, Poll}
import cats.effect.std.Semaphore
import cats.effect.syntax.all._
import cats.syntax.all._

/**
 * `F.uncancelable { poll => poll(fa) }` 与 `fa` 等效，也就是说poll其实是把uncancelable给抵消掉了
 */
object MonadCancelApp extends IOApp.Simple {

  def guarded[F[_], R, A, E](s: Semaphore[F], alloc: F[R])
                            (use: R => F[A])
                            (release: R => F[Unit])
                            (implicit F: MonadCancel[F, E]): F[A] = F.uncancelable { poll: Poll[F] =>
    for {
      r <- alloc
      _ <- poll(s.acquire).onCancel(release(r))
      releaseAll = s.release >> release(r)
      a <- poll(use(r)).guarantee(releaseAll)
    } yield a
  }

  /*
   * Self Cancelation
   * MonadCancel[F].canceled会将后续逻辑短路。
   * 但是如果它被包在`uncancelable`代码块中（且没有被poll抵消掉），那么在代码块内部`canceled`将被压制（视作`().pure[F]`），
   * 而一旦`uncancelable`代码块结束，`canceled`状态将会重新生效（也就是说这个压制作用仅限于代码块内部）
   * cancelation不能被撤销，只能被压制（不像error可以用attempt或handleError来恢复至正常执行状态），这个特性可用于保证避免死锁
   */
  val selfCancelation = for {
    fib <- (IO.uncancelable(_ =>
      IO.canceled >> IO.println("This will print as cancelation is suppressed")
    ) >> IO.println(
      "This will never be called as we are canceled as soon as the uncancelable block finishes"
    )).start
    res <- fib.join
  } yield res

  val run = for {
    res <- selfCancelation
    _ <- IO.println(res)
  } yield ()
}
