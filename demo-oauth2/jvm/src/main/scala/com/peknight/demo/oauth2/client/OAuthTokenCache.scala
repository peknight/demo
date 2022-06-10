package com.peknight.demo.oauth2.client

case class OAuthTokenCache(accessToken: Option[String], refreshToken: Option[String], scope: Option[Set[String]])