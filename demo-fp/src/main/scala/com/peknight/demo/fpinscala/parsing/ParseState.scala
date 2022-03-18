package com.peknight.demo.fpinscala.parsing

/**
 * ParseState wraps a Location and provides some extra convenience functions.
 * isSliced indicates if the current parser is surround by a slice combinator. This lets us avoid
 * building up values that will end up getting thrown away.
 * There are several convenience functions on ParseState to make implementing some of the combinators easier.
 */
case class ParseState(loc: Location, isSliced: Boolean) {
  def advanceBy(numChars: Int): ParseState = copy(loc = loc.copy(offset = loc.offset + numChars))
  def input: String = loc.input.substring(loc.offset)
  def unslice = copy(isSliced = false)
  def reslice(s: ParseState) = copy(isSliced = s.isSliced)
  def slice(n: Int) = loc.input.substring(loc.offset, loc.offset + n)
}
