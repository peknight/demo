package com.peknight.demo.js.lihaoyi.handson.webpage

import scala.scalajs.js

object Weather {

  def celsius(kelvins: js.Dynamic) = {
    kelvins.asInstanceOf[Double] - 273.15
  }.toInt
}
