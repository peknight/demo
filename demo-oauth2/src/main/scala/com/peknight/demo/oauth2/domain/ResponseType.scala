package com.peknight.demo.oauth2.domain

enum ResponseType(val value: String) derives CanEqual:
  case Code extends ResponseType("code")
  case Token extends ResponseType("token")

object ResponseType:
  def fromString(responseType: String): Option[ResponseType] = ResponseType.values.find(_.value == responseType)


