package com.peknight.demo.neo4j.showcase

final case class Person(id: Long, born: Int, name: Option[String], notExists: Option[Int])
