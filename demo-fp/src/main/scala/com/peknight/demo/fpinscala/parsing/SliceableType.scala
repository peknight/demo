package com.peknight.demo.fpinscala.parsing

object SliceableType:
  type Parser[+A] = ParseState => Result[A]
