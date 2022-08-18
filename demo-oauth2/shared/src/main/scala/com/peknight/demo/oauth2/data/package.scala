package com.peknight.demo.oauth2

import cats.Functor
import cats.data.OptionT
import cats.syntax.functor.*
import org.http4s.{Uri, UrlForm}

package object data:
  extension[F[_] : Functor, A] (fa: F[A])
    def optionT = OptionT(fa.map(Option.apply))
  end extension

  extension (body: UrlForm)
    // TODO 这里的chain是否就是javascript中请求的数组？
    def getListParam(key: String): List[String] = body.get(key).toList
    def getParam(key: String): Option[String] = body.get(key).find(_.nonEmpty)
    def getValue[T](key: String)(f: String => Option[T]): Option[T] = body.get(key).map(f).find(_.isDefined).flatten
    def getUri(key: String): Option[Uri] = getValue[Uri](key)(Uri.fromString(_).toOption)
  end extension

  extension (list: List[String])
    def mapOption[T](f: String => Option[T]): List[T] =
      list.foldRight(List.empty[T])((value, list) => f(value).fold(list)(_ :: list))
  end extension
