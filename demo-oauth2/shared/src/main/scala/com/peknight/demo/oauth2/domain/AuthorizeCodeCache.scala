package com.peknight.demo.oauth2.domain

case class AuthorizeCodeCache(authorizationEndpointRequest: AuthorizeParam, scope: Set[String], user: Option[String])
