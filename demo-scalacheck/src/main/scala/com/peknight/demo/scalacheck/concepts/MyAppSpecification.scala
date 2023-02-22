package com.peknight.demo.scalacheck.concepts

import org.scalacheck.Properties

object MyAppSpecification extends Properties("MyApp"):
  include(StringSpecification)

