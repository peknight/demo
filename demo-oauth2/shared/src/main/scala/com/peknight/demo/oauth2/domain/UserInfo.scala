package com.peknight.demo.oauth2.domain

import cats.effect.Concurrent
import io.circe.Codec
import org.http4s.EntityDecoder
import org.http4s.circe.*

case class UserInfo(sub: Option[String], preferredUsername: Option[String], name: Option[String], email: Option[String],
                    emailVerified: Option[Boolean], username: Option[String] = None, password: Option[String] = None,
                    familyName: Option[String] = None, givenName: Option[String] = None,
                    middleName: Option[String] = None, nickname: Option[String] = None, profile: Option[String] = None,
                    picture: Option[String] = None, website: Option[String] = None, gender: Option[String] = None,
                    birthdate: Option[String] = None, zoneInfo: Option[String] = None, locale: Option[String] = None,
                    updatedAt: Option[String] = None, address: Option[String] = None, phoneNumber: Option[String] = None,
                    phoneNumberVerified: Option[String] = None)

object UserInfo:
  given Codec[UserInfo] = Codec.forProduct22(
    "sub",
    "preferred_username",
    "name",
    "email",
    "email_verified",
    "username",
    "password",
    "family_name",
    "given_name",
    "middle_name",
    "nickname",
    "profile",
    "picture",
    "website",
    "gender",
    "birthdate",
    "zoneinfo",
    "locale",
    "updated_at",
    "address",
    "phone_number",
    "phone_number_verified"
  )(
    (
      sub: Option[String],
      preferredUsername: Option[String],
      name: Option[String],
      email: Option[String],
      emailVerified: Option[Boolean],
      username: Option[String],
      password: Option[String],
      familyName: Option[String],
      givenName: Option[String],
      middleName: Option[String],
      nickname: Option[String],
      profile: Option[String],
      picture: Option[String],
      website: Option[String],
      gender: Option[String],
      birthdate: Option[String],
      zoneInfo: Option[String],
      locale: Option[String],
      updatedAt: Option[String],
      address: Option[String],
      phoneNumber: Option[String],
      phoneNumberVerified: Option[String]
    ) =>
      UserInfo(sub, preferredUsername, name, email, emailVerified, username, password, familyName, givenName,
        middleName, nickname, profile, picture, website, gender, birthdate, zoneInfo, locale, updatedAt, address,
        phoneNumber, phoneNumberVerified
      )
  )(t => (t.sub, t.preferredUsername, t.name, t.email, t.emailVerified, t.username, t.password, t.familyName,
    t.givenName, t.middleName, t.nickname, t.profile, t.picture, t.website, t.gender, t.birthdate, t.zoneInfo, t.locale,
    t.updatedAt, t.address, t.phoneNumber, t.phoneNumberVerified
  ))

  given [F[_]: Concurrent]: EntityDecoder[F, UserInfo] = jsonOf[F, UserInfo]
