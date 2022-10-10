package com.peknight.demo.zio.overview

import zio.*

object RunningEffectsApp:

  val myRuntime: Runtime[Int] = Runtime(ZEnvironment[Int](42), FiberRefs.empty, RuntimeFlags.default)