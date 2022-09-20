package com.peknight.demo.oauth2.common

import cats.data.NonEmptyList
import com.peknight.demo.oauth2.domain.{AuthMethod, GrantType, ResponseType}
import io.circe.{Decoder, Json}
import org.http4s.Uri

trait Mapper[F[_], A, B]:
  def fMap(a: A): F[B]
end Mapper

object Mapper extends App:
  given [A] (using decoder: Decoder[A]): Mapper[Option, Json, A] with
    def fMap(a: Json): Option[A] = decoder.decodeJson(a).toOption

  given [A, B] (using mapper: Mapper[Option, A, B]): Mapper[Option, List[A], List[B]] with
    def fMap(a: List[A]): Option[List[B]] =
      Some(a.foldRight(List.empty[B])((a, acc) => mapper.fMap(a).fold(acc)(_ :: acc)))
  given [A]: Mapper[Option, List[A], NonEmptyList[A]] with
    def fMap(a: List[A]): Option[NonEmptyList[A]] = a match
      case head :: tail => Some(NonEmptyList(head, tail))
      case Nil => None
  given Mapper[Option, String, Uri] with
    def fMap(a: String): Option[Uri] = Uri.fromString(a).toOption
  given Mapper[Option, String, AuthMethod] with
    def fMap(a: String): Option[AuthMethod] = AuthMethod.fromString(a)
  given Mapper[Option, String, GrantType] with
    def fMap(a: String): Option[GrantType] = GrantType.fromString(a)
  given Mapper[Option, String, ResponseType] with
    def fMap(a: String): Option[ResponseType] = ResponseType.fromString(a)
end Mapper
