package com.peknight.demo.fpinscala.parsing

case class ParseError(stack: List[(Location, String)] = List(), otherFailures: List[ParseError] = List()) {
  def push(loc: Location, msg: String): ParseError = copy(stack = (loc, msg) :: stack)
  def label(msg: String): ParseError = ParseError(latestLoc.map((_, msg)).toList)
  def latestLoc: Option[Location] = latest.map(_._1)
  def latest: Option[(Location, String)] = stack.lastOption

  /**
   * Display collapsed error stack - any adjacent stack elements with the same location are combined on one line.
   * For the bottommost error, we display the full line, with a caret pointing to the column of the error.
   */
  override def toString: String =
    if (stack.isEmpty) "no error message"
    else {
      val collapsed = collapseStack(stack)
      val context = collapsed.lastOption.map("\n\n" + _._1.currentLine).getOrElse("") +
        collapsed.lastOption.map("\n" + _._1.columnCaret).getOrElse("")
      collapsed.map { case (loc, msg) => loc.line.toString + "." + loc.col + " " + msg }.mkString("\n") + context
    }

  /*
   * Builds a collapsed version of the given error stack -
   * messages at the same location have their messages merged,
   * separated by semicolons
   */
  def collapseStack(s: List[(Location, String)]): List[(Location, String)] =
    s.groupBy(_._1).view.mapValues(_.map(_._2).mkString("; ")).toList.sortBy(_._1.offset)

  def formatLoc(l: Location): String = s"${l.line}.${l.col}"

  // Exercise 9.18
  def addFailure(e: ParseError): ParseError = this.copy(otherFailures = e :: this.otherFailures)
}
