package com.peknight.demo.oauth2.domain

import io.circe.{Decoder, Encoder}

enum CodeChallengeMethod(val value: String) derives CanEqual:
  case S256 extends CodeChallengeMethod("S256")
  case Plain extends CodeChallengeMethod("plain")

object CodeChallengeMethod:
  def fromString(codeChallengeMethod: String): Option[CodeChallengeMethod] =
    CodeChallengeMethod.values.find(_.value == codeChallengeMethod)

  given Encoder[CodeChallengeMethod] = Encoder.encodeString.contramap[CodeChallengeMethod](_.value)

  given Decoder[CodeChallengeMethod] =
    Decoder.decodeString.emap[CodeChallengeMethod](str => fromString(str).toRight(s"No such CodeChallengeMethod: $str"))

