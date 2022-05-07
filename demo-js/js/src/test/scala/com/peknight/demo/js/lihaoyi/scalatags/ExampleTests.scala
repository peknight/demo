package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.generic.Bundle
import utest.*

class ExampleTests[Builder, Output <: FragT, FragT](bundle: Bundle[Builder, Output, FragT]) extends TestSuite:
  import bundle.*
  def tests = Tests {
    // ...
  }
end ExampleTests

object ExampleTests:
  object ExampleTextTests extends ExampleTests(scalatags.Text)
  object ExampleJsDomTests extends ExampleTests(scalatags.JsDom)
end ExampleTests

