package com.peknight.demo.oauth2.common

import cats.data.{NonEmptyList, ValidatedNel}
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.common.UrlFragment.*

import java.util.UUID
import scala.collection.immutable.BitSet
import scala.concurrent.duration.Duration
import scala.util.Try

trait UrlFragmentDecoder[A]:
  def decode(fragment: UrlFragment): ValidatedNel[String, A]

object UrlFragmentDecoder:

  def apply[A](using decoder: UrlFragmentDecoder[A]): UrlFragmentDecoder[A] = decoder

  extension (fragment: UrlFragment)
    def decode[A](using decoder: UrlFragmentDecoder[A]): ValidatedNel[String, A] = decoder.decode(fragment)
  end extension

  given UrlFragmentDecoder[Unit] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Unit] = ().validNel[String]

  def parseValue[A](fragment: UrlFragment)(f: String => A): ValidatedNel[String, A] = fragment match
    case UrlFragmentValue(value) => Try(f(value)).fold(_.toString.invalidNel[A], _.validNel[String])
    case fragment => s"Can not parse $fragment".invalidNel[A]

  given UrlFragmentDecoder[Boolean] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Boolean] = parseValue(fragment)(_.toBoolean)

  given UrlFragmentDecoder[Byte] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Byte] = parseValue(fragment)(_.toByte)

  given UrlFragmentDecoder[Short] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Short] = parseValue(fragment)(_.toShort)

  given UrlFragmentDecoder[Int] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Int] = parseValue(fragment)(_.toInt)

  given UrlFragmentDecoder[Long] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Long] = parseValue(fragment)(_.toLong)

  given UrlFragmentDecoder[Float] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Float] = parseValue(fragment)(_.toFloat)

  given UrlFragmentDecoder[Double] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Double] = parseValue(fragment)(_.toDouble)

  given UrlFragmentDecoder[BigInt] with
    def decode(fragment: UrlFragment): ValidatedNel[String, BigInt] = parseValue(fragment)(BigInt.apply)

  given UrlFragmentDecoder[BigDecimal] with
    def decode(fragment: UrlFragment): ValidatedNel[String, BigDecimal] = parseValue(fragment)(BigDecimal.apply)


  given UrlFragmentDecoder[Char] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Char] = fragment match
      case UrlFragmentValue(value) if value.length == 1 => value.head.validNel[String]
      case fragment => s"Can not parse $fragment".invalidNel[Char]

  given UrlFragmentDecoder[Symbol] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Symbol] = parseValue(fragment)(Symbol.apply)

  given UrlFragmentDecoder[String] with
    def decode(fragment: UrlFragment): ValidatedNel[String, String] = fragment match
      case UrlFragmentValue(value) => value.validNel[String]
      case fragment => s"Can not parse $fragment".invalidNel[String]

  given UrlFragmentDecoder[UUID] with
    def decode(fragment: UrlFragment): ValidatedNel[String, UUID] = parseValue(fragment)(UUID.fromString)

  given UrlFragmentDecoder[Duration] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Duration] = parseValue(fragment)(Duration.apply)


  given [A] (using decoder: UrlFragmentDecoder[A]): UrlFragmentDecoder[Option[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Option[A]] = fragment match
      case UrlFragmentNone => none[A].validNel[String]
      case fragment => decoder.decode(fragment).map(_.some)

  given [A] (using decoder: UrlFragmentDecoder[A]): UrlFragmentDecoder[List[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, List[A]] = fragment match
      case UrlFragmentObject(listMap) =>
        listMap.foldRight(List.empty[(Int, UrlFragment)].asRight[NonEmptyList[String]]) { case ((key, value), either) =>
          for
            list <- either
            index <- Try(key.toInt).toEither.leftMap(e => NonEmptyList.one(e.toString))
          yield (index, value) :: list
        }.flatMap(list => list.sortBy(_._1).foldLeft((Vector.empty[A], 0).asRight[NonEmptyList[String]]) {
          case (either, (index, fragment)) =>
            lazy val decodeNone = decoder.decode(UrlFragmentNone).toEither
            for
              tuple <- either
              (vector, i) = tuple
              prepend <-
                if i < index then decodeNone.map(a => Vector.fill(index - i)(a))
                else Vector.empty[A].asRight[NonEmptyList[String]]
              a <- decoder.decode(fragment).toEither
            yield (vector :++ prepend :+ a, index + 1)
        }.map(_._1.to(List))).toValidated
      case fragment => s"Can not parse $fragment".invalidNel[List[A]]

end UrlFragmentDecoder
