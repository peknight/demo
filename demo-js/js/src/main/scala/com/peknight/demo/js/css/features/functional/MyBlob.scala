package com.peknight.demo.js.css.features.functional

import japgolly.univeq.*

case class MyBlob(blobIsHappy: Boolean)

object MyBlob:
  given UnivEq[MyBlob] = UnivEq.derive
