package com.peknight.demo.cats.monad

import cats.{Id, Monad}
import com.peknight.demo.cats.functor.Tree
import com.peknight.demo.cats.functor.Tree.{Branch, Leaf}

import scala.annotation.tailrec

object MonadInstances:
  val idDemoMonad: DemoMonad[Id] = new DemoMonad[Id]:
    def pure[A](a: A): Id[A] = a
    override def map[A, B](value: Id[A])(func: A => B): Id[B] = func(value)
    def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] = f(value)

  val idMonad: Monad[Id] = new Monad[Id]:
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Id[Either[A, B]]): Id[B] = f(a) match
      case Right(b) => b
      case Left(la) => tailRecM(la)(f)
    def pure[A](x: A): Id[A] = x

  val optionMonad: Monad[Option] = new Monad[Option]:
    def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] = opt.flatMap(fn)
    def pure[A](opt: A): Option[A] = Some(opt)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] = f(a) match
      case None => None
      case Some(Left(a1)) => tailRecM(a1)(f)
      case Some(Right(b)) => Some(b)

  def treeMonadNonTailRecursive: Monad[Tree] = new Monad[Tree]:
    def pure[A](x: A): Tree[A] = Tree.leaf(x)

    def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match
      case Branch(left, right) => Tree.branch(flatMap(left)(f), flatMap(right)(f))
      case Leaf(leaf) => f(leaf)

    def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = flatMap(f(a)) {
      case Left(value) => tailRecM(value)(f)
      case Right(value) => Leaf(value)
    }

  given treeMonad: Monad[Tree] with
    def pure[A](x: A): Tree[A] = Tree.leaf(x)
    def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match
      case Branch(left, right) => Tree.branch(flatMap(left)(f), flatMap(right)(f))
      case Leaf(leaf) => f(leaf)
    // see: https://stackoverflow.com/questions/44504790/cats-non-tail-recursive-tailrecm-method-for-monads
    // 把树展开了再重构
    def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      @tailrec
      def loop(open: List[Tree[Either[A, B]]], closed: List[Option[Tree[B]]]): List[Tree[B]] = open match
        case Branch(l, r) :: next => loop(l :: r :: next, None :: closed)
        case Leaf(Left(value)) :: next => loop(f(value):: next, closed)
        case Leaf(Right(value)) :: next => loop(next, Some(pure(value)) :: closed)
        case Nil => closed.foldLeft(Nil: List[Tree[B]]) { (acc: List[Tree[B]], maybeTree: Option[Tree[B]]) =>
          maybeTree.map(_ :: acc) // Option[List[Tree[B]]]
            .getOrElse {
              val left :: right :: tail = acc
              Tree.branch(left, right) :: tail
            }
        }
      loop(List(f(a)), Nil).head
