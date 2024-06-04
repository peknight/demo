package com.peknight.demo.fpinscala.iomonad

sealed class IORef[A](var value: A):
  def set(a: A): IO[A] = IO { value = a; a }
  def get: IO[A] = IO { value }
  def modify(f: A => A): IO[A] = get.flatMap(a => set(f(a)))
