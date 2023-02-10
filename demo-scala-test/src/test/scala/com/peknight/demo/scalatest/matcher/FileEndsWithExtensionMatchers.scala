package com.peknight.demo.scalatest.matcher

import org.scalatest.matchers.{MatchResult, Matcher}

trait FileEndsWithExtensionMatchers:
  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File]:
    def apply(left: java.io.File) =
      val name = left.getName
      MatchResult(
        name.endsWith(expectedExtension),
        s"""File $name did not end with extension "$expectedExtension"""",
        s"""File $name ended with extension "$expectedExtension""""
      )

  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)

object FileEndsWithExtensionMatchers extends FileEndsWithExtensionMatchers