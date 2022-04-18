package com.peknight.demo.scala3.abstractmembers

object AbstractMembersApp extends App:
  println(US.make(3015))
  val yen = Japan.Yen.from(US.Dollar * 100)
  println(yen)
  val euros = Europe.Euro.from(yen)
  println(euros)
  val dollars = US.Dollar.from(euros)
  println(dollars)
  println(US.Dollar * 100 + dollars)
