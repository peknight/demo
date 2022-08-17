package com.peknight.demo.oauth2.domain

enum AuthMethod(val value: String) derives CanEqual:
  case SecretBasic extends AuthMethod("secret_basic")
  case SecretPost extends AuthMethod("secret_post")
  case None extends AuthMethod("none")
  case ClientSecretBasic extends AuthMethod("client_secret_basic")
  case ClientSecretPost extends AuthMethod("client_secret_post")

object AuthMethod:
  def fromString(authMethod: String): Option[AuthMethod] = AuthMethod.values.find(_.value == authMethod)


