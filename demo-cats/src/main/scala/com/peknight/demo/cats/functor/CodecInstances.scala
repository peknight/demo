package com.peknight.demo.cats.functor

object CodecInstances:

  given stringCodec: Codec[String] with
    def encode(value: String): String = value
    def decode(value: String): String = value

  given intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)

  given booleanCodec: Codec[Boolean] = stringCodec.imap(_.toBoolean, _.toString)

  given doubleCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)

  given boxCodec[A](using c: Codec[A]): Codec[Box[A]] = c.imap(Box(_), _.value)