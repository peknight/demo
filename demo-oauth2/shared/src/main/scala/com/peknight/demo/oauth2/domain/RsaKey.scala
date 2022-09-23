package com.peknight.demo.oauth2.domain

// d是私钥的核心字段
case class RsaKey(alg: String, d: String, e: String, n: String, kty: String, kid: String)
