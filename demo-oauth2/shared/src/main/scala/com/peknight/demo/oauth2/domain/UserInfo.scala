package com.peknight.demo.oauth2.domain

import io.circe.Codec

case class UserInfo(sub: String, preferredUsername: String, name: String, email: String, emailVerified: Boolean,
                    username: Option[String] = None, password: Option[String] = None)

object UserInfo:
  given Codec[UserInfo] = Codec.forProduct7(
    "sub",
    "preferred_username",
    "name",
    "email",
    "email_verified",
    "username",
    "password"
  )(
    (
      sub: String,
      preferredUsername: String,
      name: String,
      email: String,
      emailVerified: Boolean,
      username: Option[String],
      password: Option[String]
    ) =>
      UserInfo(sub, preferredUsername, name, email, emailVerified, username, password)
  )(t => (t.sub, t.preferredUsername, t.name, t.email, t.emailVerified, t.username, t.password))