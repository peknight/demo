package com.peknight.demo.scala.enums

enum Hope[+T] derives CanEqual:
  case Glad(o: T)
  case Sad
