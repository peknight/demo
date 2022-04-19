package com.peknight.demo.scala3.enums

enum Hope[+T] derives CanEqual:
  case Glad(o: T)
  case Sad
