package com.peknight.demo.js.css.features.functional

import scalacss.DevDefaults.*

object MyStyles extends StyleSheet.Standalone:

  import dsl.*

  for i <- 0 to 3 yield s".indent-$i" - paddingLeft(i * 2.ex)

  Domain.boolean                                   // Domain[Boolean]
  Domain.ofValues(1, 5, 7, 9)                      // Domain[Int]
  Domain.ofRange(0 to 4)                           // Domain[Int]
  Domain.ofRange(0 to 4).option *** Domain.boolean // Domain[(Option[Int], Boolean)]
