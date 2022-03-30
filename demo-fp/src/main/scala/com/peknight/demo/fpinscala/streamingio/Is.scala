package com.peknight.demo.fpinscala.streamingio

case class Is[I]() {
  sealed trait f[X]
  val Get = new f[I] {}
}
