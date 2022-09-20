package com.peknight.demo.oauth2.domain

import pdi.jwt.JwtClaim

case class OAuthTokenCache(accessToken: Option[String], refreshToken: Option[String], scope: Option[Set[String]],
                           idToken: Option[JwtClaim])
