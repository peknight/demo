package com.peknight.demo.scala.marcos.mirror.philippmartini

import com.peknight.demo.scala.marcos.mirror.philippmartini.PrettyString.{*, given}

/**
 * 参考：https://blog.philipp-martini.de/blog/magic-mirror-scala3/
 */
object MirrorApp extends App :

  prettyPrintln(User("bob", 25))

  // println(labelFromMirror[User])

  // println(getElemLabelsHelpler[User])

  // println(summonInstancesHelper[User])

  // val userPrettyString = derivePrettyStringCaseClass[User]
  // println(userPrettyString.prettyString(User("Bob", 25)))

  val visitorPrettyString = derived[Visitor]
  val visitors = List(Visitor.User("Bob", 25), Visitor.AnonymousVisitor)
  visitors.foreach(visitor => println(visitorPrettyString.prettyString(visitor)))
