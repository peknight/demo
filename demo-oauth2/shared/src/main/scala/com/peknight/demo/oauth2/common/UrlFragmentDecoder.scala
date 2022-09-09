package com.peknight.demo.oauth2.common

import cats.data.ValidatedNel

trait UrlFragmentDecoder[A]:
  def decode(fragment: UrlFragment): ValidatedNel[String, A]

object UrlFragmentDecoder:

  def apply[A](using decoder: UrlFragmentDecoder[A]): UrlFragmentDecoder[A] = decoder

  extension (fragment: UrlFragment)
    def decode[A](using decoder: UrlFragmentDecoder[A]): ValidatedNel[String, A] = decoder.decode(fragment)


