package com.peknight.demo.fpinscala.parsing

import java.util.regex.Pattern
import scala.language.implicitConversions
import scala.util.matching.Regex

// Parser is a type parameter that itself is a covariant type constructor
trait Parsers[Parser[+_]] { self =>
  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  implicit def string(s: String): Parser[String]
  implicit def operators[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)
  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  // Here the Parser type constructor is applied to Char.
  def char(c: Char): Parser[Char] = string(c.toString).map(_.charAt(0))

//  def defaultSucceed[A](a: A): Parser[A] = string("") map (_ => a)

  def succeed[A](a: A): Parser[A]

  // 获取匹配Parser的原始输入字符串的那一部分 如`run(slice(('a'|'b').many))("aaba")`结果为`Right("aaba")`（而不是`Right(List('a', 'a', 'b', 'a'))`）
  def slice[A](p: Parser[A]): Parser[String]

  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p))(_ :: _)

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] =
    if (n <= 0) succeed(List.empty[A])
    else map2(p, listOfN(n - 1, p))(_ :: _)

  def many[A](p: Parser[A]): Parser[List[A]] = or(map2(p, many(p)/*wrap(many(p))*/)(_ :: _), succeed(List.empty[A]))

  def or[A](s1: Parser[A], s2: => Parser[A]): Parser[A]

  def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B]

  implicit def regex(r: Regex): Parser[String]

  def product[A, B](p: Parser[A], p2: => Parser[B]): Parser[(A, B)] = for {
    a <- p
    b <- p2
  } yield (a, b)

//  def map2[A, B, C](a: Parser[A], b: => Parser[B])(f: (A, B) => C): Parser[C] = map(product(a, b))(f.tupled)

  def map2[A, B, C](p: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] = for {
    a <- p
    b <- p2
  } yield f(a, b)

  def map[A, B](a: Parser[A])(f: A => B): Parser[B] = flatMap(a)(a => succeed(f(a)))

  def label[A](msg: String)(p: Parser[A]): Parser[A]

  /*
   * Unlike label, scope doesn't throw away the label(s) attached to p
   * it merely adds additional information in the event that p fails.
   */
  def scope[A](msg: String)(p: Parser[A]): Parser[A]

  /**
   * attempt(p flatMap (_ => fail)) or p2 == p2
   */
  def attempt[A](p: Parser[A]): Parser[A]

  /**
   * Sequences two parsers, ignoring the result of the first. We wrap the ignored half in slice,
   * since we don't care about its result
   */
  def skipL[B](p: Parser[Any], p2: => Parser[B]): Parser[B] = map2(slice(p), p2)((_, b) => b)

  /**
   * Sequences two parsers, ignoring the result of the second. We wrap the ignored half in slice,
   * since we don't care about its result
   */
  def skipR[A](p: Parser[A], p2: => Parser[Any]): Parser[A] = map2(p, slice(p2))((a, _) => a)

  def opt[A](p: Parser[A]): Parser[Option[A]] = p.map(Some(_)) or succeed(None)

  /** Parser which consumes zero or more whitespace characters. */
  def whitespace: Parser[String] = "\\s*".r

  /** Parser which consumes 1 or more digits */
  def digits: Parser[String] = "\\d+".r

  /** Parser which consumes reluctantly until it encounters the given string. */
  def thru(s: String): Parser[String] = (".*?"+Pattern.quote(s)).r

  /** Unescaped string literals, like "foo" or "bar" */
  def quoted: Parser[String] = string("\"") *>  thru("\"").map(_.dropRight(1))

  /** Unescaped or escaped string literals, like "An \n important \"Quotation\"" or "bar" */
  def escapedQuoted: Parser[String] =
    token(string("\"") *> "([^\\\\\"]|\\\\[^\"])*+(\\\\\"([^\\\\\"]|\\\\[^\"])*+)*+\"".r.map(_.dropRight(1)))

  /**
   * C/Java style floating point literals, e.g.1, -1.0, 1e9, 1E-23, etc.
   * Result is left as aa string to keep full precision
   */
  def doubleString: Parser[String] = token("[-+]?([0-9]*\\.)?[0-9]+([eE][-+]?[0-9]+)?".r)

  /** Floating point literals, converted to a Double */
  def double: Parser[Double] = doubleString map (_.toDouble) label "double literal"

  /** Attempts p and strips trailing whitespace, usually used for the tokens of a grammar */
  def token[A](p: Parser[A]): Parser[A] = attempt(p) <* whitespace

  /** Zero or more repetitions of p, separated by p2, whose results are ignored. */
  def sep[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] = sep1(p, p2) or succeed(List.empty[A])

  /** One or more repetitions of p, separated by p2, whose results are ignored. */
  def sep1[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] = map2(p, many(p2 *> p))(_ :: _)

  /** Parses a sequence of left-associative binary operators with the same precedence. */
  def opL[A](p: Parser[A])(op: Parser[(A, A) => A]): Parser[A] =
    map2(p, many(op ** p))((h, t) => t.foldLeft(h)((a, b) => b._1(a, b._2)))

  /** Wraps p in start/stop delimiters. */
  def surround[A](start: Parser[Any], stop: Parser[Any])(p: => Parser[A]) =  start *> p <* stop

  /** A parser that succeeds when given empty input */
  def eof: Parser[String] = regex("\\z".r).label("unexpected trailing characters")

  /** The root of the grammar, expecteds no further input following p. */
  def root[A](p: Parser[A]): Parser[A] = p <* eof

  case class ParserOps[A](p: Parser[A]) {
    // Use self to explicitly disambiguate reference to the or method on the trait
    def |[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)

    def map[B](f: A => B): Parser[B] = self.map(p)(f)
    def many: Parser[List[A]] = self.many(p)

    def slice: Parser[String] = self.slice(p)

    def **[B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)
    def product[B](p2: => Parser[B]): Parser[(A, B)] = self.product(p, p2)

    def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)

    def label(msg: String): Parser[A] = self.label(msg)(p)

    def scope(msg: String): Parser[A] = self.scope(msg)(p)

    def *>[B](p2: => Parser[B]) = self.skipL(p, p2)
    def <*(p2: => Parser[Any]) = self.skipR(p, p2)
    def token = self.token(p)
    def sep(separator: Parser[Any]) = self.sep(p, separator)
    def sep1(separator: Parser[Any]) = self.sep1(p, separator)
    def as[B](b: B): Parser[B] = self.map(self.slice(p))(_ => b)
    def opL(op: Parser[(A, A) => A]): Parser[A] = self.opL(p)(op)

    def many1: Parser[List[A]] = self.many1(p)
  }

  object Laws {
    import com.peknight.demo.fpinscala.testing.Prop._
    import com.peknight.demo.fpinscala.testing._
    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop = forAll(in)(s => self.run(p1)(s) == self.run(p2)(s))
    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop = equal(p, p.map(a => a))(in)
    def labelLaw[A](p: Parser[A], inputs: SGen[String]): Prop = forAll(inputs ** Gen.string) { case (input, msg) =>
      self.run(label(msg)(p))(input) match {
        case Left(e: ParseError) => errorMessage(e) == msg
        case _ => true
      }
    }
  }

  // We could introduce a combinator, `wrap`:
//  def wrap[A](p: => Parser[A]): Parser[A]

//  def errorLocation(e: ParseError): Location

  def errorMessage(e: ParseError): String

  def fail[A](msg: String): Parser[A]

  // exercise 9.11
  /** In the event of an error, returns the error that occurred after consuming the most number of characters. */
//  def furthest[A](p: Parser[A]): Parser[A]

  /** In the event of an error, returns the error that occurred most recently */
//  def latest[A](p: Parser[A]): Parser[A]

  // parse: 1a 2aa 3aaa
  for {
    digit <- "[0-9]+".r
    n = digit.toInt // we really should catch exceptions thrown by toInt and convert to parse failure
    _ <- listOfN(n, char('a'))
  } yield n

  //  map(many(char('a')))(_.size)
  val numA: Parser[Int] = char('a').many.slice.map(_.size)

  val numAB: Parser[(Int, Int)] = char('a').many.slice.map(_.size) ** char('b').many1.slice.map(_.size)
}
