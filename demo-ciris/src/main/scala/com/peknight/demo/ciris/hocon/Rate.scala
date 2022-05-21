package com.peknight.demo.ciris.hocon

import java.time.Period
import scala.concurrent.duration.FiniteDuration

case class Rate(elements: Int, burstDuration: FiniteDuration, checkInterval: Period)
