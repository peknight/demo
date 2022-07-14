package com.peknight.demo.oauth2.domain

enum GrantType(val value: String) derives CanEqual:
  case AuthorizationCode extends GrantType("authorization_code")
  case RefreshToken extends GrantType("refresh_token")
  case ClientCredentials extends GrantType("client_credentials")
  case Password extends GrantType("password")

object GrantType:
  def fromString(grantType: String): Option[GrantType] = GrantType.values.find(_.value == grantType)

