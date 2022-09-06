package com.peknight.demo.scala.meta.mirror.philippmartini

enum Visitor derives PrettyString :
  // User like before, but extending Visitor
  case User(name: String, age: Int)
  // A user that is not registered visiting our website
  case AnonymousVisitor

