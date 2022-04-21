package com.peknight.demo.fpinscala.state

import com.peknight.demo.fpinscala.state.State.*

import scala.annotation.tailrec

case class State[S, +A](run: S => (A, S)):
  // Exercise 6.10
  def map[B](f: A => B): State[S, B] = flatMap(a => unit(f(a)))

  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    flatMap(a => sb.map(b => f(a, b)))

  def flatMap[B](f: A => State[S, B]): State[S, B] = State { s =>
    val (a, s2) = run(s)
    f(a).run(s2)
  }

end State

object State:

  def unit[S, A](a: A): State[S, A] = State((a, _))

  def sequence[S, A](fs: List[State[S, A]]): State[S, List[A]] =
    fs.foldRight(unit[S, List[A]](List()))((f, acc) => f.map2(acc)(_ :: _))

  def sequenceWithRecursive[S, A](sas: List[State[S, A]]): State[S, List[A]] =
    @tailrec def go(s: S, actions: List[State[S, A]], acc: List[A]): (List[A], S) = actions match
      case Nil => (acc.reverse, s)
      case h :: t => h.run(s) match
        case (a, s2) => go(s2, t, a :: acc)
    State((s: S) => go(s, sas, List()))

  def sequenceViaFoldLeft[S, A](l: List[State[S, A]]): State[S, List[A]] =
    l.reverse.foldLeft(unit[S, List[A]](List()))((acc, f) => f.map2(acc)(_ :: _))

  def modify[S](f: S => S): State[S, Unit] =
    for
      s <- get
      _ <-set(f(s))
    yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

end State
