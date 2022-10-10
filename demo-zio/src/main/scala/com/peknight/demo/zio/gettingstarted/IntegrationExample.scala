package com.peknight.demo.zio.gettingstarted

import zio.*

object IntegrationExample extends App:

  val runtime: Runtime[Any] = Runtime.default

  Unsafe.unsafe { unsafe =>
    given Unsafe = unsafe
    runtime.unsafe.run(ZIO.attempt(println("Hello World!"))).getOrThrowFiberFailure()
  }
