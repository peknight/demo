package com.peknight.demo.cats.functor

trait Codec[A] { self =>
  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B]:
    def encode(value: B): String = self.encode(enc(value))
    def decode(value: String): B = dec(self.decode(value))
}
object Codec {
  def encode[A](value: A)(using c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(using c: Codec[A]): A = c.decode(value)
}
