package com.peknight.demo.fpinscala.parsing

import scala.language.implicitConversions

// Parser is a type parameter that itself is a covariant type constructor
trait Parsers[ParseError, Parser[+_]] { self =>
  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  // Here the Parser type constructor is applied to Char.
  def char(c: Char): Parser[Char] = string(c.toString).map(_.charAt(0))

  def or[A](s1: Parser[A], s2: Parser[A]): Parser[A]

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]]

  def many[A](p: Parser[A]): Parser[List[A]]

  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p))(_ :: _)

  def succeed[A](a: A): Parser[A] = string("") map (_ => a)

  def map[A, B](a: Parser[A])(f: A => B): Parser[B]

  def map2[A, B, C](a: Parser[A], b: Parser[B])(f: (A, B) => C): Parser[C] = map(product(a, b))(f.tupled)

  // 获取匹配Parser的原始输入字符串的那一部分 如`run(slice(('a'|'b').many))("aaba")`结果为`Right("aaba")`（而不是`Right(List('a', 'a', 'b', 'a'))`）
  def slice[A](p: Parser[A]): Parser[String]

  def product[A, B](p: Parser[A], p2: Parser[B]): Parser[(A, B)]

  implicit def string(s: String): Parser[String]

  implicit def operators[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)

  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  case class ParserOps[A](p: Parser[A]) {
    // Use self to explicitly disambiguate reference to the or method on the trait
    def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)
    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
    def many: Parser[List[A]] = self.many(p)
    def map[B](f: A => B): Parser[B] = self.map(p)(f)
    def slice: Parser[String] = self.slice(p)
    def **[B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)
    def product[B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)
  }

  //  map(many(char('a')))(_.size)
  val numA: Parser[Int] = char('a').many.slice.map(_.size)

  object Laws {
    import com.peknight.demo.fpinscala.testing.Prop._
    import com.peknight.demo.fpinscala.testing._
    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop = forAll(in)(s => self.run(p1)(s) == self.run(p2)(s))
    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop = equal(p, p.map(a => a))(in)
  }

}
