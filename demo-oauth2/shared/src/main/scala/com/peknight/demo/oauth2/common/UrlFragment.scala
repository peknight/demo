package com.peknight.demo.oauth2.common

import cats.data.NonEmptyList

import scala.collection.immutable.ListMap
import com.peknight.demo.oauth2.common.UrlFragment.*

sealed trait UrlFragment derives CanEqual:
  def toFragment: String
  def toFragment(keyMapper: String => String): String

object UrlFragment:
  case class UrlFragmentValue(value: String) extends UrlFragment:
    def toFragment: String = value
    def toFragment(keyMapper: String => String): String = value

  case class UrlFragmentObject(value: ListMap[String, UrlFragment]) extends UrlFragment:
    def toFragment: String = flatten.map { case (keys, value) =>
      s"${keys.head}${keys.tail.map(key => s"[$key]").mkString}=$value"
    }.mkString("&")
    def toFragment(keyMapper: String => String): String = flatten.map { case (keys, value) =>
      s"${keyMapper(keys.head)}${keys.tail.map(key => s"[${keyMapper(key)}]").mkString}=$value"
    }.mkString("&")
    private[this] def flatten: ListMap[NonEmptyList[String], String] = value.flatMap {
      case (key, UrlFragmentValue(value)) => ListMap(NonEmptyList.one(key) -> value)
      case (key, obj: UrlFragmentObject) => obj.flatten.map { case (tailKey, value) => (key :: tailKey, value) }
      case (_, UrlFragmentNone) => ListMap.empty[NonEmptyList[String], String]
    }
  case object UrlFragmentNone extends UrlFragment:
    def toFragment: String = ""
    def toFragment(keyMapper: String => String): String = ""

  def toUrlFragmentObject(listMap: ListMap[NonEmptyList[String], String]): UrlFragmentObject =
    ???
