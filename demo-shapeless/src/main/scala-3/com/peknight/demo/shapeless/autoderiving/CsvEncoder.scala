package com.peknight.demo.shapeless.autoderiving

import shapeless3.deriving.{Continue, K0}

trait CsvEncoder[A]:
  def encode(value: A): List[String]

object CsvEncoder:

  def apply[A](using enc: CsvEncoder[A]): CsvEncoder[A] = enc

  def instance[A](func: A => List[String]): CsvEncoder[A] = func(_)

  given csvEncoderSum[A](using inst: => K0.CoproductInstances[CsvEncoder, A]): CsvEncoder[A] with
    def encode(value: A): List[String] =
      inst.erasedMap(value)((i, _) => i.asInstanceOf[CsvEncoder[A]].encode(value)).asInstanceOf

  given csvEncoderProduct[A](using inst: => K0.ProductInstances[CsvEncoder, A]): CsvEncoder[A] with
    def encode(value: A): List[String] = inst.foldRight[List[String]](value)(Nil)(
      [T] => (csvEncoder: CsvEncoder[T], t: T, acc: List[String]) => Continue(csvEncoder.encode(t) ::: acc)
    )

  inline def derived[A](using gen: K0.Generic[A]): CsvEncoder[A] = gen.derive(csvEncoderProduct, csvEncoderSum)

  given CsvEncoder[String] with
    def encode(value: String): List[String] = List(value)

  given CsvEncoder[Int] with
    def encode(value: Int): List[String] = List(value.toString)

  given CsvEncoder[Boolean] with
    def encode(value: Boolean): List[String] = List(if value then "yes" else "no")

  given CsvEncoder[Double] with
    def encode(value: Double): List[String] = List(value.toString)

  def writeCsv[A](values: List[A])(using enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")
