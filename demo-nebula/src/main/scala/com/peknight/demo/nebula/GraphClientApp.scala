package com.peknight.demo.nebula

import com.vesoft.nebula.{Date, Value}
import com.vesoft.nebula.client.graph.NebulaPoolConfig
import com.vesoft.nebula.client.graph.data.{CASignedSSLParam, HostAddress, ResultSet, SSLParam, SelfSignedSSLParam}
import com.vesoft.nebula.client.graph.net.{NebulaPool, Session}

import java.util.concurrent.TimeUnit
import scala.jdk.CollectionConverters.*

object GraphClientApp:

  val hostAddress: HostAddress = new HostAddress("127.0.0.1", 9669)
  val addresses: java.util.List[HostAddress] = List(hostAddress).asJava

  def main(args: Array[String]): Unit =
    val pool: NebulaPool = new NebulaPool
    try
      val nebulaPoolConfig: NebulaPoolConfig = new NebulaPoolConfig
      nebulaPoolConfig.setMaxConnSize(100)
      val initResult: Boolean = pool.init(addresses, nebulaPoolConfig)
      if !initResult then
        println("pool init failed.")
      else
        val session: Session = pool.getSession("root", "", false)
        demo1(session)
        TimeUnit.SECONDS.sleep(5)
        demo2(session)
        demo3(session)
        demo4(session)
        demo5(session)
        demo6(session)
        demo7()
        demo8()
    catch
      case e: Exception => e.printStackTrace()
  end main

  def demo1(session: Session): Unit =
    val createSchema: String =
      """
        |CREATE SPACE IF NOT EXISTS test(vid_type=fixed_string(20));
        |USE test;
        |CREATE TAG IF NOT EXISTS person(name string, age int);
        |CREATE EDGE IF NOT EXISTS like(likeness double)
      """.stripMargin
    demo(session, createSchema)

  def demo2(session: Session): Unit =
    val insertVertexes =
      """
        |INSERT VERTEX person(name, age) VALUES
        |'Bob': ('Bob', 10),
        |'Lily': ('Lily', 9),
        |'Tom': ('Tom', 10),
        |'Jerry': ('Jerry', 13),
        |'John': ('John', 11);
      """.stripMargin
    demo(session, insertVertexes)

  def demo3(session: Session): Unit =
    val insertEdges =
      """
        |INSERT EDGE like(likeness) VALUES
        |'Bob'->'Lily':(80.0),
        |'Bob'->'Tom':(70.0),
        |'Lily'->'Jerry':(84.0),
        |'Tom'->'Jerry':(68.3),
        |'Bob'->'John':(97.2);
      """.stripMargin
    demo(session, insertEdges)

  def demo4(session: Session): Unit =
    val query =
      """
        |GO FROM "Bob" OVER like
        |YIELD $^.person.name, $^.person.age, like.likeness
      """.stripMargin
    demo(session, query)

  def demo5(session: Session): Unit =
    val nvalue = new Value
    val date = new Date
    date.setYear(2021)
    nvalue.setDVal(date)
    val list = List[Any](1, true, nvalue, date).asJava
    val map = Map[String, Any]("a" -> 1, "b" -> true, "c" -> nvalue, "d" -> list).asJava
    val paramMap = Map[String, Any]("p1" -> 3, "p2" -> true, "p3" -> 3.3, "p4" -> list, "p5" -> map).asJava
    val query =
      """
        |RETURN abs($p1+1),toBoolean($p2) and false,$p3,$p4[2],$p5.d[3]
      """.stripMargin
    printResult(query, session.executeWithParameter(query, paramMap))

  def demo6(session: Session): Unit =
    val queryForJson = "YIELD 1"
    demoJson(session, queryForJson)

  def demo7(): Unit =
    demoSsl(new CASignedSSLParam(
      "demo-nebula/src/main/resources/ssl/casigned.pem",
      "demo-nebula/src/main/resources/ssl/casigned.crt",
      "demo-nebula/src/main/resources/ssl/casigned.key"
    ))

  def demo8(): Unit =
    demoSsl(new SelfSignedSSLParam(
      "demo-nebula/src/main/resources/ssl/selfsigned.pem",
      "demo-nebula/src/main/resources/ssl/selfsigned.key",
      "vesoft"
    ))

  def demo(session: Session, stmt: String): Unit =
    printResult(stmt, session.execute(stmt))

  def demoJson(session: Session, stmt: String): Unit =
    val resp: String = session.executeJson(stmt)
    io.circe.parser.parse(resp) match
      case Right(json) =>
        val errorOpt =
          for
            jsonObject <- json.asObject
            errors <- jsonObject("errors")
            errorArray <- errors.asArray
            errorHead <- errorArray.headOption
            errorObj <- errorHead.asObject
          yield errorObj
        val codeOpt =
          for
            errorObj <- errorOpt
            code <- errorObj("code")
            codeNum <- code.asNumber
            codeInt <- codeNum.toInt
          yield codeInt
        codeOpt match
          case Some(code) if code != 0 =>
            val messageOpt =
              for
                errorObj <- errorOpt
                message <- errorObj("message")
                messageStr <- message.asString
              yield messageStr
            println(s"Execute: $stmt, failed: ${messageOpt.getOrElse("")}")
            System.exit(1)
          case _ => println(resp)
      case Left(e) => println(s"parse error: $e")

  def demoSsl(sslParam: SSLParam): Unit =
    val nebulaSslPoolConfig: NebulaPoolConfig = new NebulaPoolConfig
    nebulaSslPoolConfig.setMaxConnSize(100)
    nebulaSslPoolConfig.setEnableSsl(true)
    nebulaSslPoolConfig.setSslParam(sslParam)
    val sslPool: NebulaPool = new NebulaPool
    sslPool.init(addresses, nebulaSslPoolConfig)
    val queryForJson = "YIELD 1"
    val sslSession = sslPool.getSession("root", "", false)
    demoJson(sslSession, queryForJson)

  def printResult(schema: String, resp: ResultSet): Unit =
    if !resp.isSucceeded then
      println(s"Execute: $schema, failed: ${resp.getErrorMessage}")
      System.exit(1)
    else
      val keys = resp.keys().asScala.map(k => String.format("%15s |", k)).mkString
      val values = List.tabulate(resp.rowsSize())(resp.rowValues).map(record => record.values().asScala.map {
        case value if value.isLong => String.format("%15s |", value.asLong())
        case value if value.isBoolean => String.format("%15s |", value.asBoolean())
        case value if value.isDouble => String.format("%15s |", value.asDouble())
        case value if value.isString => String.format("%15s |", value.asString())
        case value if value.isTime => String.format("%15s |", value.asTime())
        case value if value.isDate => String.format("%15s |", value.asDate())
        case value if value.isDateTime => String.format("%15s |", value.asDateTime())
        case value if value.isVertex => String.format("%15s |", value.asNode())
        case value if value.isEdge => String.format("%15s |", value.asRelationship())
        case value if value.isPath => String.format("%15s |", value.asPath())
        case value if value.isList => String.format("%15s |", value.asList())
        case value if value.isSet => String.format("%15s |", value.asSet())
        case value if value.isMap => String.format("%15s |", value.asMap())
      }.mkString).mkString("\n")
      println(s"$keys\n$values")

end GraphClientApp
