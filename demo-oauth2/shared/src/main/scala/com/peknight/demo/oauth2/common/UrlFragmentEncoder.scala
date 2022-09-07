package com.peknight.demo.oauth2.common

import com.peknight.demo.oauth2.common.UrlFragment.*
import org.http4s.AuthScheme
import shapeless3.deriving.{Continue, K0, Labelling}

import java.util.UUID
import scala.collection.immutable.*
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}

trait UrlFragmentEncoder[T]:
  def encode(value: T): UrlFragment

object UrlFragmentEncoder:

  def apply[A](using encoder: UrlFragmentEncoder[A]): UrlFragmentEncoder[A] = encoder

  trait UrlFragmentValueEncoder[T] extends UrlFragmentEncoder[T]:
    def encode(value: T): UrlFragmentValue

  def fromToString[A]: UrlFragmentValueEncoder[A] = (value: A) => UrlFragmentValue(value.toString)

  def fromToString[A](value: A): UrlFragmentValue = UrlFragmentValue(value.toString)

  def encodeIterable[A](value: Iterable[A])(using encoder: UrlFragmentEncoder[A]): UrlFragment =
    UrlFragmentObject(ListMap.from(value.zipWithIndex.map {
      case (v, index) => (index.toString, encoder.encode(v))
    }))

  def encodeMap[K, V](value: Map[K, V])(using kEncoder: UrlFragmentValueEncoder[K], vEncoder: UrlFragmentEncoder[V])
  : UrlFragment = UrlFragmentObject(ListMap.from(value.map {
    case (k, v) => (kEncoder.encode(k).value, vEncoder.encode(v))
  }))

  given UrlFragmentEncoder[Unit] with
    def encode(value: Unit): UrlFragment = UrlFragmentNone
  given UrlFragmentValueEncoder[Boolean] with
    def encode(value: Boolean): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Byte] with
    def encode(value: Byte): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Short] with
    def encode(value: Short): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Int] with
    def encode(value: Int): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Long] with
    def encode(value: Long): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Float] with
    def encode(value: Float): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Double] with
    def encode(value: Double): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[BigInt] with
    def encode(value: BigInt): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[BigDecimal] with
    def encode(value: BigDecimal): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Char] with
    def encode(value: Char): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Symbol] with
    def encode(value: Symbol): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[String] with
    def encode(value: String): UrlFragmentValue = UrlFragmentValue(value)
  given UrlFragmentValueEncoder[UUID] with
    def encode(value: UUID): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[Duration] with
    def encode(value: Duration): UrlFragmentValue = fromToString(value)
  given UrlFragmentValueEncoder[BitSet] with
    def encode(value: BitSet): UrlFragmentValue = fromToString(value)
  given [A] (using encoder: UrlFragmentEncoder[A]): UrlFragmentEncoder[Option[A]] with
    def encode(value: Option[A]): UrlFragment = value match
      case Some(v) => encoder.encode(v)
      case None => UrlFragmentNone
  given [A] (using encoder: UrlFragmentEncoder[A]): UrlFragmentEncoder[Try[A]] with
    def encode(value: Try[A]): UrlFragment = value match
      case Success(v) => encoder.encode(v)
      case _ => UrlFragmentNone

  given [A: UrlFragmentEncoder]: UrlFragmentEncoder[List[A]] with
    def encode(value: List[A]): UrlFragment = encodeIterable(value)

  given [A: UrlFragmentEncoder]: UrlFragmentEncoder[Vector[A]] with
    def encode(value: Vector[A]): UrlFragment = encodeIterable(value)

  given [A: UrlFragmentEncoder]: UrlFragmentEncoder[Queue[A]] with
    def encode(value: Queue[A]): UrlFragment = encodeIterable(value)

  given [A, B] (using aEncoder: UrlFragmentEncoder[A], bEncoder: UrlFragmentEncoder[B]): UrlFragmentEncoder[Either[A, B]] with
    def encode(value: Either[A, B]): UrlFragment = value match
      case Left(a) => aEncoder.encode(a)
      case Right(b) => bEncoder.encode(b)

  given [A: UrlFragmentEncoder]: UrlFragmentEncoder[Set[A]] with
    def encode(value: Set[A]): UrlFragment = encodeIterable(value)

  given [K: UrlFragmentValueEncoder, V: UrlFragmentEncoder]: UrlFragmentEncoder[Map[K, V]] with
    def encode(value: Map[K, V]): UrlFragment = encodeMap(value)

  given [A: UrlFragmentEncoder]: UrlFragmentEncoder[SortedSet[A]] with
    def encode(value: SortedSet[A]): UrlFragment = encodeIterable(value)

  given [K: UrlFragmentValueEncoder, V: UrlFragmentEncoder]: UrlFragmentEncoder[SortedMap[K, V]] with
    def encode(value: SortedMap[K, V]): UrlFragment = encodeMap(value)

  given UrlFragmentValueEncoder[AuthScheme] with
    def encode(value: AuthScheme): UrlFragmentValue = fromToString(value)

  def urlFragmentEncoderSum[A](using inst: => K0.CoproductInstances[UrlFragmentEncoder, A]): UrlFragmentEncoder[A] =
    (value: A) => inst.ordinal(value).asInstanceOf[UrlFragmentEncoder[A]].encode(value)

  def urlFragmentEncoderProduct[A](using inst: => K0.ProductInstances[UrlFragmentEncoder, A], labelling: Labelling[A])
  : UrlFragmentEncoder[A] = (value: A) => UrlFragmentObject(ListMap.from(labelling.elemLabels.zip(
    inst.foldRight[List[UrlFragment]](value)(Nil)(
      [T] => (urlFragmentEncoder: UrlFragmentEncoder[T], t: T, acc: List[UrlFragment]) =>
        Continue(urlFragmentEncoder.encode(t) :: acc)
    )
  )))

  inline given derived[A](using gen: K0.Generic[A]): UrlFragmentEncoder[A] =
    gen.derive(urlFragmentEncoderProduct, urlFragmentEncoderSum)

  extension [A] (a: A)
    def toFragment(using encoder: UrlFragmentEncoder[A]): String = encoder.encode(a).toFragment
    def toFragment(keyMapper: String => String)(using encoder: UrlFragmentEncoder[A]): String =
      encoder.encode(a).toFragment(keyMapper)
