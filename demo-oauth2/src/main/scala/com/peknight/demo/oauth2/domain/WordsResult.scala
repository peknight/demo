package com.peknight.demo.oauth2.domain

enum WordsResult(val value: String) derives CanEqual:
  case Get extends WordsResult("get")
  case NoGet extends WordsResult("noget")
  case Add extends WordsResult("add")
  case NoAdd extends WordsResult("noadd")
  case Rm extends WordsResult("rm")
  case NoRm extends WordsResult("norm")

object WordsResult:
  def fromString(value: String): Option[WordsResult] = WordsResult.values.find(_.value == value)
