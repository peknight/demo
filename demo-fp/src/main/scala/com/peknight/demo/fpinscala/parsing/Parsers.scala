package com.peknight.demo.fpinscala.parsing

import scala.language.implicitConversions

// Parser is a type parameter that itself is a covariant type constructor
trait Parsers[ParseError, Parser[+_]] { self =>
  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  // Here the Parser type constructor is applied to Char.
  def char(c: Char): Parser[Char]

  def or[A](s1: Parser[A], s2: Parser[A]): Parser[A]

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]]

  def many[A](p: Parser[A]): Parser[List[A]]

  def map[A, B](a: Parser[A])(f: A => B): Parser[B]

  implicit def string(s: String): Parser[String]

  implicit def operators[A](p: Parser[A]) = ParserOps[A](p)

  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  case class ParserOps[A](p: Parser[A]) {
    // Use self to explicitly disambiguate reference to the or method on the trait
    def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)
    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
    def many: Parser[List[A]] = self.many(p)
    def map[B](f: A => B): Parser[B] = self.map(p)(f)
  }

  //  map(many(char('a')))(_.size)
  val numA: Parser[Int] = char('a').many.map(_.size)

  object Laws {
    import com.peknight.demo.fpinscala.testing.Prop._
    import com.peknight.demo.fpinscala.testing._
    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop = forAll(in)(s => run(p1)(s) == run(p2)(s))
    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop = equal(p, p.map(a => a))(in)
  }

}
