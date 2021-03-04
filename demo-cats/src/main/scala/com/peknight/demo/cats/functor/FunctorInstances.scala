package com.peknight.demo.cats.functor

import cats.Functor
import com.peknight.demo.cats.functor.Tree.{Branch, Leaf}

object FunctorInstances {
  implicit def boxFunctor: Functor[Box] = new Functor[Box] {
    def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
  }

  val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  import scala.concurrent.{Future, ExecutionContext}

  def futureFunctor(implicit ec: ExecutionContext): Functor[Future] = new Functor[Future] {
    def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
  }

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      fa match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(value) => Leaf(f(value))
      }
    }
  }

}
