package com.peknight.demo.scala.meta.quoted

import scala.quoted.*

object QuotedPatterns:
  def valueOfBoolean(x: Expr[Boolean])(using Quotes): Option[Boolean] =
    x match
      case '{ true } => Some(true)
      case '{ false } => Some(false)
      case _ => None

  object MatchingExactExpression:
    def valueOfBooleanOption(x: Expr[Option[Boolean]])(using Quotes): Option[Option[Boolean]] =
      x match
        case '{ Some(true) } => Some(Some(true))
        case '{ Some(false) } => Some(Some(false))
        case '{ None } => Some(None)
        case _ => None
  end MatchingExactExpression

  def valueOfBooleanOption(x: Expr[Option[Boolean]])(using Quotes): Option[Option[Boolean]] =
    x match
      case '{ Some($boolExpr: Boolean) } => Some(valueOfBoolean(boolExpr))
      case '{ None } => Some(None)
      case _ => None

  def exprOfOption[T : Type](x: Expr[Option[T]])(using Quotes): Option[Expr[T]] =
    x match
      case '{ Some($x: T) } => Some(x)
      case '{ None } => None
      case _ => None

  def valueOf(x: Expr[Any])(using Quotes): Option[Any] =
    x match
      case '{ $x: Boolean } => valueOfBoolean(x)
      case '{ $x: Option[Boolean] } => valueOfBooleanOption(x)
      case _ => None

  def exprOfOptionOf[T : Type](x: Expr[Option[Any]])(using Quotes): Option[Expr[T]] =
    x match
      case '{ Some($x: T) } => Some(x)
      case _ => None

  def exprOptionToList(x: Expr[Option[Any]])(using Quotes): Option[Expr[List[Any]]] =
    x match
      case '{ Some($x: t) } => Some('{ List[t]($x) })
      case '{ None } => Some('{ Nil })
      case _ => None

  def typeExpression(expr: Expr[Option[Int]])(using Quotes) =
    expr match
      case '{ $expr: tpe } =>
        // could be: Option[Int], Some[Int], None, Option[1], Option[2], ...
        Type.show[tpe]
        // binds the value without widening the type
        '{ val x: tpe = $expr; x }

  def fuseMap[T : Type](x: Expr[List[T]])(using Quotes): Expr[List[T]] = x match
    case '{
      type u
      type v
      ($ls: List[`u`])
        .map($f: `u` => `v`)
        .map($g: `v` => T)
    } => '{ $ls.map(y => $g($f(y))) }
    case _ => x

  def fuseMapV2[T : Type](x: Expr[List[T]])(using Quotes): Expr[List[T]] = x match
    case '{ ($ls: List[u]).map[v]($f).map[T]($g) } => '{ $ls.map(y => $g($f(y))) }
    case _ => x

  inline def mirrorFields[T]: List[String] = ${mirrorFieldsImpl[T]}
  def mirrorFieldsImpl[T : Type](using Quotes): Expr[List[String]] =
    def rec[A : Type]: List[String] = Type.of[A] match
      case '[field *: fields] => Type.show[field] :: rec[fields]
      case '[EmptyTuple] => Nil
      case _ => quotes.reflect.report.errorAndAbort("Expected known tuple but got: " + Type.show[A])
    Expr(rec)