package com.peknight.demo.monocle.optics

import monocle.macros.GenIso

object IsoGenerationApp extends App:
  // println(GenIso[MyString, String].get(MyString("Hello")))
  println(monocle.Focus[MyString](_.s).get(MyString("Hello")))
  // GenIso.unit[Foo]
  // GenIso.unit[Bar.type]
  // println(GenIso.fields[Person].get(Person("John", 42)))

