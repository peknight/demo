package com.peknight.demo.oauth2.domain

case class UserInfo(sub: String, preferredUsername: String, name: String, email: String, emailVerified: Boolean,
                    username: Option[String] = None, password: Option[String] = None)