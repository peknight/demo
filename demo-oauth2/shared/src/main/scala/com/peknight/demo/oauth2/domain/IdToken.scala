package com.peknight.demo.oauth2.domain

case class IdToken(issuer: Option[String], subject: Option[String], audience: Option[Set[String]],
                   expiration: Option[Long], issuedAt: Option[Long], jwtId: Option[String])
