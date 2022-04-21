package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.iomonad.App.IO
import com.peknight.demo.fpinscala.iomonad.Free.Suspend
import com.peknight.demo.fpinscala.iomonad.IOMonadApp.fahrenheitToCelsius
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
import com.peknight.demo.fpinscala.streamingio.Process.{Channel, Emit, End, Halt, constant, eval, fileW, intersperse, resource_}

import java.io.{BufferedReader, File, FileReader}
import java.sql.{Connection, PreparedStatement}

object StreamingIOApp extends App:
  def linesGt40k(filename: String): IO[Boolean] = Suspend(Par.lazyUnit {
    val src = io.Source.fromFile(filename)
    try
      var count = 0
      val lines: Iterator[String] = src.getLines()
      while count <= 40000 && lines.hasNext do
        lines.next()
        count += 1
      count > 40000
    finally src.close()
  })

  /*
   * lazy I/O. 读到文件结尾才关闭文件
   */
  def lines(filename: String): IO[LazyList[String]] = Suspend(Par.lazyUnit {
    val src = io.Source.fromFile(filename)
    src.getLines().to(LazyList).lazyAppendedAll {
      src.close()
      LazyList.empty
    }
  })

  val lines: LazyList[String] = LazyList("a", "b", "c")
  lines.zipWithIndex.exists(_._2 + 1 >= 40000)
  lines.filter(_.trim.nonEmpty).zipWithIndex.exists(_._2 + 1 >= 40000)
  lines.filter(_.trim.nonEmpty).take(40000).map(_.head).indexOfSlice("abracadabra".toList)

  val processOne = SimpleStreamTransducers.Process.liftOne((x: Int) => x * 2)
  val xsOne = processOne(LazyList(1, 2, 3)).toList
  println(xsOne)
  val processRepeat = SimpleStreamTransducers.Process.lift((x: Int) => x * 2)
  val xsRepeat = processRepeat(LazyList(1, 2, 3)).toList
  println(xsRepeat)

  val units = LazyList.continually(())
  val ones = SimpleStreamTransducers.Process.lift((_: Unit) => 1)(units)
  ones.take(1).foreach(println)

  val even = SimpleStreamTransducers.Process.filter((x: Int) => x % 2 == 0)
  val evens = even(LazyList(1, 2, 3, 4)).toList
  println(evens)

  val sums = SimpleStreamTransducers.Process.sum(LazyList(1.0, 2.0, 3.0, 4.0)).toList
  println(sums)

  SimpleStreamTransducers.Process.processFile(
    new File("lines.txt"),
    SimpleStreamTransducers.Process.count[String] |> SimpleStreamTransducers.Process.exists(_ > 40000),
    false
  )(_ || _)

  val p: Process[IO, String] = Process.await[IO, BufferedReader, String](Suspend(Par.lazyUnit(new BufferedReader(new FileReader("lines.txt"))))) {
    case Right(b) =>
      lazy val next: Process[IO, String] = Process.await[IO, String, String](Suspend(Par.lazyUnit(b.readLine()))) {
        case Left(e) => Process.await[IO, Unit, String](Suspend(Par.lazyUnit(b.close()))) { _ => Halt(e) }
        case Right(line) => if line eq null then Halt(End) else Emit(line, next)
      }
      next
    case Left(e) => Halt(e)
  }
  Process.runLog(p)

  val converter: Process[IO, Unit] = Process.lines("fahrenheit.txt")
    .filter(line => !line.startsWith("#") && line.trim.nonEmpty)
    .map(line => fahrenheitToCelsius(line.toDouble).toString)
    .pipe(intersperse("\n"))
    .to(fileW("celsius.txt"))
    .drain

  def query(conn: IO[Connection]): Channel[IO, Connection => PreparedStatement, Map[String, Any]] = resource_
    { conn }
    { conn => constant { (q: Connection => PreparedStatement) =>
      resource_
        { Suspend(Par.lazyUnit {
          val rs = q(conn).executeQuery()
          val ncols = rs.getMetaData.getColumnCount
          val cols = (1 to ncols).map(rs.getMetaData.getColumnName)
          (rs, cols)
        }) }
        { case (rs, cols) =>
          def step = if !rs.next() then None else Some(cols.map(c => (c, rs.getObject(c): Any)).toMap)
          lazy val rows: Process[IO, Map[String, Any]] =
            eval[IO, Option[Map[String, Any]]](Suspend(Par.lazyUnit(step))).flatMap {
              case None => Halt(End)
              case Some(row) => Emit(row, rows)
            }
          rows
        }
        { p => Suspend(Par.lazyUnit(p._1.close())) }
    }}
    { conn => Suspend(Par.lazyUnit(conn.close())) }

  val convertAll: Process[IO, Unit] =
    (for
      out <- fileW("celsius.txt").once
      file <- Process.lines("fahrenheits.txt")
      _ <- Process.lines(file).map(line => fahrenheitToCelsius(line.toDouble)).flatMap(celsius => out(celsius.toString))
    yield ()).drain

  val convertMultiSink: Process[IO, Unit] =
    (for
      file <- Process.lines("fahrenheits.txt")
      _ <- Process.lines(file).map(line => fahrenheitToCelsius(line.toDouble)).map(_.toString).to(fileW(file + ".celsius"))
    yield ()).drain

  val convertMultiSink2: Process[IO, Unit] =
    (for
      file <- Process.lines("fahrenheits.txt")
      _ <- Process.lines(file)
        .filter(!_.startsWith("#"))
        .map(line => fahrenheitToCelsius(line.toDouble))
        .filter(_ > 0)
        .map(_.toString)
        .to(fileW(file + ".celsius"))
    yield ()).drain

end StreamingIOApp
