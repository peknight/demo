package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object PatternMatching:
  inline def sum(inline args: Int*): Int = ${ sumExpr('args) }

  private def sumExpr(argsExpr: Expr[Seq[Int]])(using Quotes): Expr[Int] =
    argsExpr match
      case Varargs(args @ Exprs(argValues)) =>
        // args is of type Seq[Expr[Int]]
        // argValues is of type Seq[Int]
        // precompute result of sum
        Expr(argValues.sum)
      case Varargs(argExprs) =>
        // argExprs is of type Seq[Expr[Int]]
        val staticSum: Int = argExprs.map(_.value.getOrElse(0)).sum
        val dynamicSum: Seq[Expr[Int]] = argExprs.filter(_.value.isEmpty)
        dynamicSum.foldLeft(Expr(staticSum))((acc, arg) => '{ $acc + $arg })
      case _ =>
        '{ $argsExpr.sum }

  object QuotedPatterns:
    def sum(args: Int*): Int = args.sum
    inline def optimize(inline arg: Int): Int = ${ optimizeExpr('arg) }

    private def optimizeExpr(body: Expr[Int])(using Quotes): Expr[Int] =
      body match
        case '{ sum() } => Expr(0)
        case '{ sum($n) } => n
        case '{ sum(${Varargs(args)}*) } => sumExpr(args)
        case body => body

    private def sumExpr(args1: Seq[Expr[Int]])(using Quotes): Expr[Int] =
      def flatSumArgs(arg: Expr[Int]): Seq[Expr[Int]] = arg match
        case '{ sum(${ Varargs(subArgs) }*) } => subArgs.flatMap(flatSumArgs)
        case arg => Seq(arg)
      val args2 = args1.flatMap(flatSumArgs)
      val staticSum: Int = args2.map(_.value.getOrElse(0)).sum
      val dynamicSum: Seq[Expr[Int]] = args2.filter(_.value.isEmpty)
      dynamicSum.foldLeft(Expr(staticSum))((acc, arg) => '{ $acc + $arg })
  end QuotedPatterns

  extension (inline sc: StringContext)
    inline def showMe(inline args: Any*): String = ${ showMeExpr('sc, 'args) }
  end extension

  private def showMeExpr(sc: Expr[StringContext], argsExpr: Expr[Seq[Any]])(using Quotes): Expr[String] =
    import quotes.reflect.report
    argsExpr match
      case Varargs(argExprs) =>
        val argShowedExprs = argExprs.map {
          case '{ $arg: tp } =>
            Expr.summon[Show[tp]] match
              case Some(showExpr) =>
                '{ $showExpr.show($arg) }
              case None =>
                report.error(s"could not find implicit for ${Type.show[Show[tp]]}", arg); '{???}
        }
        val newArgsExpr = Varargs(argShowedExprs)
        '{ $sc.s($newArgsExpr*) }
      case _ =>
        // `new StringContext(...).showMeExpr(args: _*)` not an explicit `showMeExpr"..."`
        report.error(s"Args must be explicit", argsExpr)
        '{???}

  trait Show[-T]:
    def show(x: T): String

  // in a different file
  given Show[Boolean] with
    def show(b: Boolean) = "boolean!"


  inline def eval(inline e: Int): Int = ${ evalExpr('e) }

  private def evalExpr(e: Expr[Int])(using Quotes): Expr[Int] = e match
    case '{ val y: Int = $x; $body(y): Int } =>
      // body: Expr[Int => Int] where the argument represents
      // references to y
      evalExpr(Expr.betaReduce('{$body(${evalExpr(x)})}))
    case '{ ($x: Int) * ($y: Int) } =>
      (x.value, y.value) match
        case (Some(a), Some(b)) => Expr(a * b)
        case _ => e
    case _ => e

