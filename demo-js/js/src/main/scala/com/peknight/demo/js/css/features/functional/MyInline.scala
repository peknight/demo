package com.peknight.demo.js.css.features.functional

import com.peknight.demo.js.css.features.functional.Blah.*
import scalacss.DevDefaults.*

object MyInline extends StyleSheet.Inline:

  import dsl.*

  // Styles for each A
  // styleF(Domain[A])(a => styleS(…))

  // Convenience methods for boolean & integer ranges
  // styleF.bool      (b => styleS(…))
  // styleF.int(range)(i => styleS(…))

  // Manual classname
  // styleF("manual")(Domain[A])(a => styleS(…))
  // styleF("manual").bool      (b => styleS(…))
  // styleF("manual").int(range)(i => styleS(…))

  // Convenience method: StyleF.bool
  val everythingOk = styleF.bool(ok => styleS(backgroundColor(if ok then green else red)))

  val indent = styleF.int(1 to 3)(i => styleS(paddingLeft(i * 4.ex)))

  // Full control
  val blahDomain = Domain.ofValues(Blah1, Blah2, Blah3)
  val blahStyle = styleF(blahDomain)(b => styleS(backgroundColor(yellow)))

  // Universal Equality

  // This won't work:
  // val domain = Domain.ofValues(new Blob(true), new Blob(false))
  // styleF(domain)(blob => styleS(backgroundColor(blue)))

  // 需要UnivEq定义才行
  val domain = Domain.ofValues(MyBlob(true), MyBlob(false))
  styleF(domain)(blob => styleS(backgroundColor(red)))

