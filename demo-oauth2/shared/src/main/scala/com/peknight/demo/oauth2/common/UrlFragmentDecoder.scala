package com.peknight.demo.oauth2.common

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.validated.*
import com.peknight.demo.oauth2.common.UrlFragment.*
import org.http4s.AuthScheme
import org.typelevel.ci.CIString
import shapeless3.deriving.*

import java.util.UUID
import scala.collection.IterableFactory
import scala.collection.immutable.*
import scala.concurrent.duration.Duration
import scala.deriving.Mirror
import scala.util.Try

trait UrlFragmentDecoder[A]:
  def decode(fragment: UrlFragment): ValidatedNel[String, A]

object UrlFragmentDecoder:

  def apply[A](using decoder: UrlFragmentDecoder[A]): UrlFragmentDecoder[A] = decoder

  extension (fragment: String)
    def parseFragment[A](keyMapper: String => String)(using decoder: UrlFragmentDecoder[A]): ValidatedNel[String, A] =
      UrlFragment.fromFragment(fragment, keyMapper).fold(
        error => error.toString().invalidNel[A],
        frag => decoder.decode(frag)
      )
  end extension

  given UrlFragmentDecoder[Unit] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Unit] = ().validNel[String]

  def parseValue[A](fragment: UrlFragment)(f: String => A): ValidatedNel[String, A] = fragment match
    case UrlFragmentValue(value) => Try(f(value)).fold(_.toString.invalidNel[A], _.validNel[String])
    case fragment => s"Can not parse $fragment".invalidNel[A]

  def parseIterable[A, C[_]](fragment: UrlFragment, factory: IterableFactory[C])(using decoder: UrlFragmentDecoder[A])
  : ValidatedNel[String, C[A]] = fragment match
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
      }.map(_._1.to(factory))).toValidated
    case fragment => s"Can not parse $fragment".invalidNel[C[A]]

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

  given [A: UrlFragmentDecoder]: UrlFragmentDecoder[List[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, List[A]] = parseIterable(fragment, List)

  given [A: UrlFragmentDecoder]: UrlFragmentDecoder[Vector[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Vector[A]] = parseIterable(fragment, Vector)

  given [A: UrlFragmentDecoder]: UrlFragmentDecoder[Queue[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Queue[A]] = parseIterable(fragment, Queue)

  given [L, R] (using lDecoder: UrlFragmentDecoder[L], rDecoder: UrlFragmentDecoder[R]): UrlFragmentDecoder[Either[L, R]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Either[L, R]] =
      rDecoder.decode(fragment).map(_.asRight[L]).orElse(lDecoder.decode(fragment).map(_.asLeft[R]))


  given [A: UrlFragmentDecoder]: UrlFragmentDecoder[Set[A]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Set[A]] = parseIterable(fragment, Set)

  given [K, V](using kDecoder: UrlFragmentDecoder[K], vDecoder: UrlFragmentDecoder[V]): UrlFragmentDecoder[Map[K, V]] with
    def decode(fragment: UrlFragment): ValidatedNel[String, Map[K, V]] = fragment match
      case UrlFragmentObject(listMap) => listMap.foldLeft(Map.empty[K, V].asRight[NonEmptyList[String]]) {
        case (either, (k, v)) =>
          for
            map <- either
            key <- kDecoder.decode(UrlFragmentValue(k)).toEither
            value <- vDecoder.decode(v).toEither
          yield map + (key -> value)
      }.toValidated
      case fragment => s"Can not parse $fragment".invalidNel[Map[K, V]]

  given UrlFragmentDecoder[AuthScheme] with
    def decode(fragment: UrlFragment): ValidatedNel[String, AuthScheme] = parseValue(fragment)(CIString.apply)

  def urlFragmentDecoderSum[A](using inst: => K0.CoproductInstances[UrlFragmentDecoder, A]): UrlFragmentDecoder[A] =
    (fragment: UrlFragment) =>
      val failure = NonEmptyList.one(s"Can not parse $fragment").asLeft[A]
      inst.is.foldLeft(failure) { (either, decoder) =>
        either.orElse(decoder.asInstanceOf[UrlFragmentDecoder[A]].decode(fragment).toEither)
      }.toValidated

  inline def urlFragmentDecoderProduct[A](
    using
    inst: => K0.ProductInstances[UrlFragmentDecoder, A],
    mirror: Mirror.ProductOf[A]
  ): UrlFragmentDecoder[A] = (fragment: UrlFragment) => fragment match
      case UrlFragmentObject(listMap) =>
        val ((_, es), option) = inst.unfold(
          (summonValuesAsArray[mirror.MirroredElemLabels, String].toList, List(s"Can not parse $fragment"))
        ){ [T] => (acc: (List[String], List[String]), decoder: UrlFragmentDecoder[T]) =>
          val (labels, errors) = acc
          decoder.decode(listMap.getOrElse(labels.head, UrlFragmentNone)) match
            case Valid(a) => ((labels.tail, errors), Some(a))
            case Invalid(e) =>
              ((labels.tail, s"Can not parse field ${labels.head}" :: e.toList ::: errors), None)
        }
        option.toValid(NonEmptyList.fromListUnsafe(es))
      case fragment => s"Can not parse $fragment".invalidNel[A]

  inline given derived[A](using gen: K0.Generic[A]): UrlFragmentDecoder[A] =
    gen.derive(urlFragmentDecoderProduct, urlFragmentDecoderSum)

end UrlFragmentDecoder
