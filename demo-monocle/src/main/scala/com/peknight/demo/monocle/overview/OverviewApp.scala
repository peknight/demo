package com.peknight.demo.monocle.overview

import monocle.syntax.all.*

object OverviewApp extends App:
  val user = User("Anna", Address(12, "high street"))
  println(user.focus(_.name).replace("Bob"))
  println(user.focus(_.address.streetName).modify(_.toUpperCase))
  println(user.focus(_.address.streetNumber).get)

