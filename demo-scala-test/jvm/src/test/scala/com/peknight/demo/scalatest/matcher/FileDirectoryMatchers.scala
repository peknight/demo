package com.peknight.demo.scalatest.matcher

import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}

import java.io.File

trait FileDirectoryMatchers:
  class FileBePropertyMatcher extends BePropertyMatcher[File]:
    def apply(left: File) = BePropertyMatchResult(left.isFile, "file")
  class DirectoryBePropertyMatcher extends BePropertyMatcher[File]:
    def apply(left: File) = BePropertyMatchResult(left.isDirectory, "directory")

  val file = new FileBePropertyMatcher
  val directory = new DirectoryBePropertyMatcher


