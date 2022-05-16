package com.peknight.demo.doobie.fragments

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

object FragmentsApp extends IOApp.Simple:

  val y = xa.yolo
  import y.*

  val a = fr"select name from country"
  val b = fr"where code = 'USA'"
  val c = a ++ b

  def whereCode(s: String) = fr"where code = $s"
  val fra = whereCode("FRA")

  // Fragment.const是存在注入风险的
  def count(table: String) = (fr"select count(*) from" ++ Fragment.const(table)).query[Int].unique

  val countryCode: String = "NZL"

  val whereFragment: Fragment = fr"WHERE code = $countryCode"

  // 只要每个fragment都是安全构造出来的（没有使用用户提供的输入调用Fragment.const），那么最终拼接结果也是安全的
  val frag = fr"SELECT name FROM country $whereFragment"

  // 自动空格 fr0不会自动追加空格，sql插值器实际是fr0的别名
  // Fragment = Fragment("IN ( ? , ? , ? ) ")
  //noinspection DuplicatedCode
  fr"IN (" ++ List(1, 2, 3).map(n => fr"$n").intercalate(fr",") ++ fr")"
  // Fragment = Fragment("IN (?, ?, ?) ")
  //noinspection DuplicatedCode
  fr0"IN (" ++ List(1, 2, 3).map(n => fr0"$n").intercalate(fr",") ++ fr")"

  def select(name: Option[String], pop: Option[Int], codes: List[String], limit: Long) =
    // 三个可选过滤条件
    val f1 = name.map(s => fr"name LIKE $s")
    val f2 = pop.map(n => fr"population > $n")
    val f3 = codes.toNel.map(cs => Fragments.in(fr"code", cs))
    val q: Fragment = fr"SELECT name, code, population FROM country" ++
      Fragments.whereAndOpt(f1, f2, f3) ++
      fr"LIMIT $limit"
    q.query[Info]


  val run =
    for
      _ <- c.query[String].unique.quick
      // fr或const会自动追加一个空格，一般在拼接时不能担心空格的总是
      _ <- (fr"select name from country" ++ fra).query[String].quick
      _ <- count("city").quick
      _ <- frag.query[String].option.quick
      _ <- select(None, None, Nil, 10).check
      _ <- select(Some("U%"), None, Nil, 10).check
      _ <- select(Some("U%"), Some(12345), List("FRA", "GBR"), 10).check
    yield ()

