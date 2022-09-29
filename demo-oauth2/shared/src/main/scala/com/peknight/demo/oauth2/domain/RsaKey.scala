package com.peknight.demo.oauth2.domain

// d是私钥的核心字段
case class RsaKey(alg: Option[String], d: Option[String], e: String, n: String, kty: String, kid: Option[String],
                  p: Option[String], q: Option[String])
