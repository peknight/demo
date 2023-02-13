package com.peknight.demo.scalatest.fixture

import com.peknight.demo.scalatest.fixture.DbServer.*
import org.scalatest.flatspec.AnyFlatSpec

import java.io.*
import java.util.UUID.randomUUID

class LoanFixtureFlatSpec extends AnyFlatSpec:
  def withDatabase[T](testCode: Db => T): T =
    val dbName = randomUUID.toString
    // create the fixture
    val db = createDb(dbName)
    try
      // perform setup
      db.append("ScalaTest is ")
      // "loan" the fixture to the test
      testCode(db)
    // clean up the fixture
    finally removeDb(dbName)

  def withFile[T](testCode: (File, FileWriter) => T): T =
    // create the fixture
    val file = File.createTempFile("hello", "world")
    val writer = new FileWriter(file)
    try
      // set up the fixture
      writer.write("ScalaTest is ")
      // "loan" the fixture to the test
      testCode(file, writer)
    // clean up the fixture
    finally writer.close()

  // This test needs the file fixture
  "Testing" should "be productive" in withFile { (file, writer) =>
    writer.write("productive!")
    writer.flush()
    assert(file.length === 24)
  }

  // This test needs the database fixture
  "Test code" should "be readable" in withDatabase { db =>
    db.append("readable!")
    assert(db.toString === "ScalaTest is readable!")
  }

  // This test needs both the file and the database
  it should "be clear and concise" in withDatabase { db =>
    // load-fixture methods compose
    withFile { (file, writer) =>
      db.append("clear!")
      writer.write("concise!")
      writer.flush()
      assert(db.toString === "ScalaTest is clear!")
      assert(file.length === 21)
    }
  }