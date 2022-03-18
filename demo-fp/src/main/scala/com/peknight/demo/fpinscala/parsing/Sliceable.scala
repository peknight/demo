package com.peknight.demo.fpinscala.parsing

import com.peknight.demo.fpinscala.parsing.SliceableType.Parser
import com.peknight.demo.fpinscala.parsing.Result.{Failure, Slice, Success, firstNonmatchingIndex}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.util.matching.Regex

object Sliceable extends Parsers[Parser] {

  override def run[A](p: Parser[A])(input: String): Either[ParseError, A] = p(ParseState(Location(input), false)).extract(input)

  override implicit def string(s: String): Parser[String] = state => {
    val i = firstNonmatchingIndex(state.loc.input, s, state.loc.offset)
    if (i == -1) {
      if (state.isSliced) Slice(s.length) else Success(s, s.length)
    } else Failure(state.loc.advanceBy(i).toError(s"'$s'"), i != 0)
  }


  override def succeed[A](a: A): Parser[A] = _ => Success(a, 0)

//  override def slice[A](p: Parser[A]): Parser[String] = state => p(state) match {
//    case Success(_, n) => Success(state.slice(n), n)
//    case f @ Failure(_, _) => f
//    case _ => ???
//  }

  override def slice[A](p: Parser[A]): Parser[String] = state => p(state.copy(isSliced = true)).slice

  override def or[A](s1: Parser[A], s2: => Parser[A]): Parser[A] = state => s1(state) match {
    // 失败且未提交的才执行s2
    case Failure(e, false) => s2(state).mapError(_.addFailure(e))
    case r => r
  }

  override def map[A, B](p: Parser[A])(f: A => B): Parser[B] = state => p(state) match {
    case Success(get, length) => Success(f(get), length)
    case Slice(length) => Success(f(state.slice(length).asInstanceOf[A]), length)
    case f @ Failure(_, _) => f
  }

  override def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = state => p(state) match {
    case Success(get, length) => f(get)(state.advanceBy(length)).addCommit(length != 0).advanceSuccess(length)
    case Slice(length) => f(state.slice(length).asInstanceOf[A])(state.advanceBy(length)).advanceSuccess(length)
    case f @ Failure(_, _) => f
  }

  override implicit def regex(r: Regex): Parser[String] = state =>
    r.findPrefixOf(state.input) match {
      case Some(m) => if (state.isSliced) Slice(m.length) else Success(m, m.length)
      case None => Failure(state.loc.toError("regex " + r), false)
    }

  override def label[A](msg: String)(p: Parser[A]): Parser[A] = state => p(state).mapError(_.label(msg))

  override def scope[A](msg: String)(p: Parser[A]): Parser[A] = state => p(state).mapError(_.push(state.loc, msg))

  /**
   * attempt(p flatMap (_ => fail)) or p2 == p2
   */
  override def attempt[A](p: Parser[A]): Parser[A] = state => p(state).uncommit

  override def fail[A](msg: String): Parser[A] = state => Failure(state.loc.toError(msg), true)

  override def map2[A, B, C](p: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] = state => p(state) match {
    case Success(a, n) => {
      val state2 = state.advanceBy(n)
      p2(state2) match {
        case Success(b, m) => Success(f(a, b), n + m)
        case Slice(m) => Success(f(a, state2.slice(m).asInstanceOf[B]), n + m)
        // 答案代码没addCommit会导致报错位置不符合预期
        case f @ Failure(_, _) => f.addCommit(n != 0)
      }
    }
    case Slice(n) => {
      val state2 = state.advanceBy(n)
      p2(state2) match {
        case Success(b, m) => Success(f(state.slice(n).asInstanceOf[A], b), n + m)
        case Slice(m) => if (state.isSliced) Slice(n + m).asInstanceOf[Result[C]] else Success(f(state.slice(n).asInstanceOf[A], state2.slice(m).asInstanceOf[B]), n + m)
        // 答案代码没addCommit会导致报错位置不符合预期
        case f @ Failure(_, _) => f.addCommit(n != 0)
      }
    }
    case f @ Failure(_, _) => f
  }

  override def product[A, B](p: Parser[A], p2: => Parser[B]): Parser[(A, B)] = map2(p, p2)((_, _))

  override def errorMessage(e: ParseError): String = e.toString

  override def many[A](p: Parser[A]): Parser[List[A]] = state => {
    if (state.isSliced) {
      def go(p: Parser[String], offset: Int): Result[String] = p(state.advanceBy(offset)) match {
        case f @ Failure(_, true) => f
        case Failure(_, _) => Slice(offset)
        case Slice(n) => go(p, offset + n)
        case Success(_, _) => sys.error("sliced parser should not return success, only slice")
      }
      go(p.slice, 0).asInstanceOf[Result[List[A]]]
    } else {
      val buf = ListBuffer.empty[A]
      @tailrec
      def go(p: Parser[A], offset: Int): Result[List[A]] = {
        p(state.advanceBy(offset)) match {
          case Success(a, n) => {
            buf += a
            go(p, offset + n)
          }
          case f @ Failure(_, true) => f
          case Failure(_, _) => Success(buf.toList, offset)
          case Slice(n) => {
            buf += state.input.substring(offset, offset + n).asInstanceOf[A]
            go(p, offset + n)
          }
        }
      }
      go(p, 0)
    }
  }
}
