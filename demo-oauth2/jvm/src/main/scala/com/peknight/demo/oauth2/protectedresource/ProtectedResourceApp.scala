package com.peknight.demo.oauth2.protectedresource

import cats.effect.*

object ProtectedResourceApp extends IOApp.Simple:

  val run =
    for
      _ <- IO.println(ProtectedResourcePage.skeleton)
    yield ()


