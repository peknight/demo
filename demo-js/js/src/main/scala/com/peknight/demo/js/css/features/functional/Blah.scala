package com.peknight.demo.js.css.features.functional

import japgolly.univeq.*

sealed trait Blah
object Blah:
  case object Blah1 extends Blah
  case object Blah2 extends Blah
  case object Blah3 extends Blah
  given UnivEq[Blah] = UnivEq.derive
