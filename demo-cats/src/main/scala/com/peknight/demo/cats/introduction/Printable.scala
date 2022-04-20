package com.peknight.demo.cats.introduction

trait Printable[A]:
  def format(value: A): String

  // 3.5.5 Contravariant
  def contramap[B](func: B => A): Printable[B] = (b: B) => format(func(b))

object Printable:
  def format[A](value: A)(using p: Printable[A]): String = p.format(value)
  def print[A](value: A)(using Printable[A]): Unit = println(format(value))
