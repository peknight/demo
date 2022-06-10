package com.peknight.demo.oauth2.domain

enum GrantType(val value: String) derives CanEqual:
  case AuthorizationCode extends GrantType("authorization_code")
  case RefreshToken extends GrantType("refresh_token")

object GrantType:
  def fromString(grantType: String): Option[GrantType] = GrantType.values.find(_.value == grantType)

