package com.peknight.demo.scalatest.fixture

import java.util.concurrent.ConcurrentHashMap

object DbServer:
  type Db = StringBuffer
  private val databases = new ConcurrentHashMap[String, Db]
  def createDb(name: String): Db =
    val db = new StringBuffer
    databases.put(name, db)
    db
  def removeDb(name: String): Unit =
    databases.remove(name)
