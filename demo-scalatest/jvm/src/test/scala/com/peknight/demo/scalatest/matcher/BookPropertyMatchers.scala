package com.peknight.demo.scalatest.matcher

import org.scalatest.matchers.{HavePropertyMatchResult, HavePropertyMatcher}

trait BookPropertyMatchers:
  def title(expectedValue: String) =
    new HavePropertyMatcher[Book, String]:
      def apply(book: Book) = HavePropertyMatchResult(book.title == expectedValue, "title", expectedValue, book.title)

  def author(expectedValue: List[String]) =
    new HavePropertyMatcher[Book, List[String]]:
      def apply(book: Book) = HavePropertyMatchResult(book.author == expectedValue, "author", expectedValue, book.author)

  def pubYear(expectedValue: Int) =
    new HavePropertyMatcher[Book, Int]:
      def apply(book: Book) = HavePropertyMatchResult(book.pubYear == expectedValue, "pubYear", expectedValue,
        book.pubYear)