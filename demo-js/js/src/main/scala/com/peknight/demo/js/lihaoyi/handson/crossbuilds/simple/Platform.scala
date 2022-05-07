package com.peknight.demo.js.lihaoyi.handson.crossbuilds.simple

import scala.scalajs.js

object Platform:
  def format(ts: Long) = new js.Date(ts.toDouble).toISOString()
