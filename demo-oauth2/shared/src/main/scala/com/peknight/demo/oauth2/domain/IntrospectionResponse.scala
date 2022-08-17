package com.peknight.demo.oauth2.domain

import org.http4s.Uri

case class IntrospectionResponse(active: Boolean, iss: Option[Uri], sub: Option[String], scope: Option[Set[String]],
                                 clientId: Option[String])
