package com.peknight.demo.sttp.quickstart

import sttp.client4.*
import sttp.client4.logging.slf4j.Slf4jLoggingBackend

object AddLogging:
  val backend = Slf4jLoggingBackend(DefaultSyncBackend())
end AddLogging
