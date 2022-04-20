package com.peknight.demo.cats.functor

import cats.Functor
import com.peknight.demo.cats.functor.Tree.{Branch, Leaf}

import scala.concurrent.{ExecutionContext, Future}

object FunctorInstances:
  given Functor[Box] with
    def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))

  val optionFunctor: Functor[Option] = new Functor[Option]:
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)


  def futureFunctor(using ExecutionContext): Functor[Future] = new Functor[Future]:
    def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)

  given Functor[Tree] with
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))
