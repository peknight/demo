package com.peknight.demo.oauth2.common

import cats.FlatMap
import cats.data.Chain
import io.circe.{Json, JsonObject}
import org.http4s.UrlForm

trait Getter[F[_], T, K, A]:
  def fGet(t: T)(key: K): F[A]
end Getter

object Getter:

  def apply[F[_], T, K, A](using getter: Getter[F, T, K, A]): Getter[F, T, K, A] = getter

  extension [T] (t: T)
    def fGet[F[_], K, A](key: K)(using getter: Getter[F, T, K, A]): F[A] = getter.fGet(t)(key)
  end extension

  given [A] (using mapper: Mapper[Option, Json, A]): Getter[Option, Json, String, A] with
    def fGet(t: Json)(key: String): Option[A] = t.asObject.flatMap(_.apply(key)).flatMap(mapper.fMap)
  given [A] (using mapper: Mapper[Option, Json, A]): Getter[Option, JsonObject, String, A] with
    def fGet(t: JsonObject)(key: String): Option[A] = t(key).flatMap(mapper.fMap)

  given Getter[Option, UrlForm, String, String] with
    def fGet(t: UrlForm)(key: String): Option[String] = t.getFirst(key)
  given Getter[Option, UrlForm, String, Chain[String]] with
    def fGet(t: UrlForm)(key: String): Option[Chain[String]] = Some(t.get(key))

  given [F[_]: FlatMap, T, K, A, B](using getter: Getter[F, T, K, A], mapper: Mapper[F, A, B]): Getter[F, T, K, B] with
    def fGet(t: T)(key: K): F[B] = FlatMap[F].flatMap(getter.fGet(t)(key))(mapper.fMap)
end Getter

