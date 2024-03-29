package com.peknight.demo.oauth2.domain

case class OAuthTokenCache(accessToken: Option[String], refreshToken: Option[String], scope: Option[Set[String]],
                           idToken: Option[IdToken], key: Option[RsaKey], algorithm: Option[String])
