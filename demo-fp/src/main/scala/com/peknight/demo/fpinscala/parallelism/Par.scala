package com.peknight.demo.fpinscala.parallelism

trait Par[+A] {

}
object Par {
  def unit[A](a: => A): Par[A] = ???

  def get[A](a: Par[A]): A = ???
}
