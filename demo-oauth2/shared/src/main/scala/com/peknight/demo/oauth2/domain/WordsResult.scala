package com.peknight.demo.oauth2.domain

enum WordsResult derives CanEqual:
  case Get
  case NoGet
  case Add
  case NoAdd
  case Rm
  case NoRm
