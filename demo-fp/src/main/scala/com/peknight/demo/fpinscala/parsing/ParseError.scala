package com.peknight.demo.fpinscala.parsing

case class ParseError(stack: List[(Location, String)])
