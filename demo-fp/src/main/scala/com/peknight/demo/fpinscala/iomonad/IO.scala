package com.peknight.demo.fpinscala.iomonad

import scala.io.StdIn.readLine

trait IO[A] { self =>
  def run: A

  def map[B](f: A => B): IO[B] = new IO[B] {
    def run = f(self.run)
  }

  def flatMap[B](f: A => IO[B]): IO[B] = new IO[B] {
    def run = f(self.run).run
  }

// for old version IO
//  def ++(io: IO) = new IO {
//    def run = {
//      self.run
//      io.run
//    }
//  }


}

object IO {

  def unit[A](a: => A): IO[A] = new IO[A] {
    def run = a
  }

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]) = fa flatMap f

  def apply[A](a: => A): IO[A] = unit(a)

  def ReadLine: IO[String] = IO { readLine() }

  def PrintLine(msg: String): IO[Unit] = IO { println(msg) }

  def empty: IO[Unit] = new IO[Unit] { def run = () }

}
